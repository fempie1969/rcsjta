/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.gsma.joyn.ft;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gsma.joyn.JoynContactFormatException;
import org.gsma.joyn.JoynService;
import org.gsma.joyn.JoynServiceException;
import org.gsma.joyn.JoynServiceListener;
import org.gsma.joyn.JoynServiceNotAvailableException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * This class offers the main entry point to transfer files and to
 * receive files. Several applications may connect/disconnect to the API.
 * 
 * The parameter contact in the API supports the following formats:
 * MSISDN in national or international format, SIP address, SIP-URI
 * or Tel-URI.
 * 
 * @author Jean-Marc AUFFRET 
 */
public class FileTransferService extends JoynService {
	/**
	 * API
	 */
	private IFileTransferService api = null;
	
    /**
     * Constructor
     * 
     * @param ctx Application context
     * @param listener Service listener
     */
    public FileTransferService(Context ctx, JoynServiceListener listener) {
    	super(ctx, listener);
    }

    /**
     * Connects to the API
     */
    public void connect() {
    	ctx.bindService(new Intent(IFileTransferService.class.getName()), apiConnection, 0);
    }
    
    /**
     * Disconnects from the API
     */
    public void disconnect() {
    	try {
    		ctx.unbindService(apiConnection);
        } catch(IllegalArgumentException e) {
        	// Nothing to do
        }
    }
    
    /**
     * Returns true if connected to the service, else returns false
     * 
     * @return Boolean
     */
    public boolean isServiceConnected() {
    	return (api != null);
    }

    /**
	 * Service connection
	 */
	private ServiceConnection apiConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
        	api = IFileTransferService.Stub.asInterface(service);
        	if (serviceListener != null) {
        		serviceListener.onServiceConnected();
        	}
        }

        public void onServiceDisconnected(ComponentName className) {
        	api = null;
        	if (serviceListener != null) {
        		serviceListener.onServiceDisconnected(JoynService.Error.CONNECTION_LOST);
        	}
        }
    };
    
    /**
     * Returns the configuration of the file transfer service
     * 
     * @return Configuration
     * @throws JoynServiceException
     */
    public FileTransferServiceConfiguration getConfiguration() throws JoynServiceException {
		if (api != null) {
			try {
				return api.getConfiguration();
			} catch(Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}

    /**
     * Transfers a file to a contact. The parameter file contains the complete filename
     * including the path to be transfered. The parameter contact supports the following
     * formats: MSISDN in national or international format, SIP address, SIP-URI or
     * Tel-URI. If the format of the contact is not supported an exception is thrown.
     * 
     * @param contact Contact
     * @param filename Filename to transfer
     * @param listenet File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
	 * @throws JoynContactFormatException
     */
    public FileTransfer transferFile(String contact, String filename, FileTransferListener listener) throws JoynServiceException, JoynContactFormatException {
		if (api != null) {
			try {
				IFileTransfer ftIntf = api.transferFile(contact, filename, listener);
				return new FileTransfer(ftIntf);
			} catch(Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
    }    
    
    /**
     * Returns the list of file transfers in progress
     * 
     * @return List of file transfers
     * @throws JoynServiceException
     */
    public Set<FileTransfer> getFileTransfers() throws JoynServiceException {
		if (api != null) {
			try {
	    		Set<FileTransfer> result = new HashSet<FileTransfer>();
				List<IBinder> ftList = api.getFileTransfers();
				for (IBinder binder : ftList) {
					FileTransfer ft = new FileTransfer(IFileTransfer.Stub.asInterface(binder));
					result.add(ft);
				}
				return result;
			} catch(Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
    }    

    /**
     * Returns a current file transfer from its unique ID
     * 
     * @return File transfer or null if not found
     * @throws JoynServiceException
     */
    public FileTransfer getFileTransfer(String transferId) throws JoynServiceException {
		if (api != null) {
			try {
				return new FileTransfer(api.getFileTransfer(transferId));
			} catch(Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
    }    
    
    /**
     * Returns a current file transfer from its invitation Intent
     * 
     * @param intent Invitation intent
     * @return File transfer or null if not found
     * @throws JoynServiceException
     */
    public FileTransfer getFileTransferFor(Intent intent) throws JoynServiceException {
		if (api != null) {
			try {
				String transferId = intent.getStringExtra(FileTransferIntent.EXTRA_TRANSFER_ID);
				if (transferId != null) {
					return getFileTransfer(transferId);
				} else {
					return null;
				}
			} catch(Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
    }     
    
    /**
	 * Registers a file transfer invitation listener
	 * 
	 * @param listener New file transfer listener
	 * @throws JoynServiceException
	 */
	public void addNewFileTransferListener(NewFileTransferListener listener) throws JoynServiceException {
		if (api != null) {
			try {
				api.addNewFileTransferListener(listener);
			} catch(Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}

	/**
	 * Unregisters a file transfer invitation listener
	 * 
	 * @param listener New file transfer listener
	 * @throws JoynServiceException
	 */
	public void removeNewFileTransferListener(NewFileTransferListener listener) throws JoynServiceException {
		if (api != null) {
			try {
				api.removeNewFileTransferListener(listener);
			} catch(Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}
}