package com.conetex.justus.study;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by Matthias on 17.02.2017.
 */
public class ReadMail{

	public static void main(String[] args) {
		// Properties props = new Properties();
		try{
			// props.load(new FileInputStream(new File("C:\\smtp.properties")));

			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");

			store.connect("imap.gmx.net", "mat-franke@gmx.de", "munir1205");

			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
			int messageCount = inbox.getMessageCount();

			System.out.println("Total Messages:- " + messageCount);

			Message[] messages = inbox.getMessages();
			System.out.println("------------------------------");
			for(int i = 0; i < 10; i++){
				System.out.println("Mail Subject:- " + messages[i].getSubject());
			}
			inbox.close(true);
			store.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
