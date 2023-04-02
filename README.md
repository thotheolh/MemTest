# MemTest
JavaCard persistent and transient memory enquiry applet

# Steps to install and use
1. Delete all applets with AID of "5555555555" before installing. On GPPro, run the following command:

		java -jar gp.jar --delete 5555555555

2. Install the MemTest Applet either by compiling from the source 'src' folder or via downloading the already compiled in the 'bin' folder. All releases are timestamped followed by the day's versioning number.

3. Install MemTest applet to card. On GPPro, run the following command:

		java -jar gp.jar --install '/path/to/memtest.cap'

	And please kindly replace the path of the cap file correctly....

	You should get something similar:

			(base) admin@admin:~/Desktop$ java -jar gp.jar --install '/home/admin/Desktop/memtest.cap' 
			Warning: no keys given, defaulting to 404142434445464748494A4B4C4D4E4F
			CAP loaded

4. Run the APDU by first selecting the CAP then send the enquiry APDU. On GPPro, run the following command:

		java -jar gp.jar --apdu 00A40400055555555555 --apdu 0000000000 --debug

	You need to have the debug flag to see the APDU exchanges.

	You should get the following similar response:

		(base) admin@admin:~/Desktop$ java -jar gp.jar --apdu 00A40400055555555555 --apdu 0000000000 --debug
		SCardConnect("XYZ Card reader [SCSI] 00 00", T=*) -> T=0, 3B1B96506F6C6172697320210111
		# GlobalPlatformPro v20.06.19-0-g7fd1544
		# Running on Linux 5.19.0-38-generic amd64, Java 1.8.0_352 by Temurin
		A>> T=0 (4+0005) 00A40400 05 5555555555  // <--- Selecting MemTest Applet
		A<< (0000+2) (7ms) 9000                  // <--- Returns SW == 0x9000 which means applet selected successfully
		A>> T=0 (4+0000) 00000000 00             // <--- Enquire memory APDU command
		A<< (0012+2) (4ms) 000333690000060800000608 9000 // <--- The last two bytes 0x9000 which means executed. The result 00033 .. 0608 is the memory in the 															 card.
		A>> T=0 (4+0000) 00A40400 00 
		A<< (0093+2) (12ms) 6F5B8408A000000003000000A54F734906072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6B03640B06092A864886FC6B040255650B06092A864886FC6B020101660C060A2B060104012A026E01029F6501FF 9000
		Warning: no keys given, defaulting to 404142434445464748494A4B4C4D4E4F
		SCardDisconnect("XYZ Card reader [SCSI] 00 00", true) tx:20/rx:111


5. Dissecting the returned memory result inside your current card.

		<First 4 bytes - Persistent Mmeory><Second 4 bytes - Resettable RAM memory><Last 4 bytes - Deselectable RAM memory>

	We take the above execution as an example with `000333690000060800000608` which are binary hex. You need to convert them to decimals as shown below.

		00033369 00000608 00000608
		 |        |        |
		 |        |        +-------- 1544 bytes free deselectable type RAM memory on card
		 |        |
		 |        +----------------- 1544 bytes free resettable type RAM memory on card
		 |
		 +-------------------------- 209769 bytes free persistent memory (flash or eeprom on card)



