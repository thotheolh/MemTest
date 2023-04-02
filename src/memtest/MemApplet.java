package memtest;

import javacard.framework.JCSystem;
import javacard.framework.ISOException;
import javacard.framework.ISO7816;
import javacard.framework.*;

public class MemApplet extends Applet {

	public short[] s0 = JCSystem.makeTransientShortArray((short) 2, JCSystem.CLEAR_ON_DESELECT);

	public static void install(byte[] bArray, short bOffset, byte bLength) {
		new MemApplet();
	}

	protected MemApplet() {
		register();
	}

	public void process(APDU apdu) {
		if (selectingApplet()) {
			return;
		}

		final byte[] buffer = apdu.getBuffer();
		final byte ins = buffer[ISO7816.OFFSET_INS];
		short len = apdu.setIncomingAndReceive();

		switch (ins) {
		case (byte) 0x00:
			// Set Persistent Memory Length
			JCSystem.getAvailableMemory(s0, (short) 0, JCSystem.MEMORY_TYPE_PERSISTENT);
			Util.setShort(buffer, (short) 0, s0[0]);
			Util.setShort(buffer, (short) 2, s0[1]);
			
			// Set Transient Reset Memory Length
			JCSystem.getAvailableMemory(s0, (short) 0, JCSystem.MEMORY_TYPE_TRANSIENT_RESET);
			Util.setShort(buffer, (short) 4, s0[0]);
			Util.setShort(buffer, (short) 6, s0[1]);
						
			// Set Transient Deselect Memory Length
			JCSystem.getAvailableMemory(s0, (short) 0, JCSystem.MEMORY_TYPE_TRANSIENT_DESELECT);
			Util.setShort(buffer, (short) 8, s0[0]);
			Util.setShort(buffer, (short) 10, s0[1]);
			
			apdu.setOutgoingAndSend((short) 0, (short) 12);			
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
}