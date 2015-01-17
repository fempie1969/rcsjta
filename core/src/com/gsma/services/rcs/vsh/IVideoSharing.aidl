package com.gsma.services.rcs.vsh;

import com.gsma.services.rcs.vsh.IVideoPlayer;
import com.gsma.services.rcs.contacts.ContactId;
import com.gsma.services.rcs.vsh.VideoDescriptor;

/**
 * Video sharing interface
 */
interface IVideoSharing {

	String getSharingId();

	ContactId getRemoteContact();

	int getState();

	int getReasonCode();

	int getDirection();
	
	void acceptInvitation(IVideoPlayer player);

	void rejectInvitation();

	void abortSharing();
	
	String getVideoEncoding();

	long getTimeStamp();

	long getDuration();
	
	VideoDescriptor getVideoDescriptor();
}
