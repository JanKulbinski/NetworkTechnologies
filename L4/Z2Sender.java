import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/*
 *  wysyła w osobnych datagramach po jednym znaku wczytanym z wejścia do portu o numerze podanym jako drugi parametr wywołania programu.
 *   Jednocześnie drukuje na wyjściu informacje o pakietach otrzymanych w porcie podanym jako pierwszy parametr wywołania
 */
class Z2Sender
{
	static final int datagramSize=50;
	static final int sleepTime=200;
	static final int maxPacket=50;
	
	static final int interval = 10;
	static final int waitForConfirmationTime=5000;
	
	ArrayList<Integer> buffor;
	volatile int expectedToRead = 0;
	int firstInBuffor = 0;
	
	InetAddress localHost;
	int destinationPort;
	DatagramSocket socket;
	SenderThread sender;
	ReceiverThread receiver;

	public Z2Sender(int myPort, int destPort)
			throws Exception
	{
		localHost=InetAddress.getByName("127.0.0.1");
		destinationPort=destPort;
		socket=new DatagramSocket(myPort);
		sender=new SenderThread();
		receiver=new ReceiverThread();
		
		buffor = new ArrayList<Integer>(interval);
	}

	class SenderThread extends Thread
	{
		
		public void readFromFile() throws IOException {
			
			int x,i;
			
			for(i = firstInBuffor; i < expectedToRead; i++)
				buffor.remove(0);
			
			firstInBuffor = expectedToRead;
			
			for(int j=0;  buffor.size() < interval && (x=System.in.read()) >= 0  ; j++)
				buffor.add(x);
	
		}
		
		public void run()
		{
			int i, x;
			
			do {
				try
				{
					readFromFile();
					for(i = 0; i < buffor.size(); i++)
					{
						x = buffor.get(i);
						Z2Packet p=new Z2Packet(4+1);
						p.setIntAt(firstInBuffor + i,0);
						p.data[4]= (byte) x;
						DatagramPacket packet = 
								new DatagramPacket(p.data, p.data.length, 
										localHost, destinationPort);
						socket.send(packet);
						sleep(sleepTime);
					}
					sleep(waitForConfirmationTime);
				}
				catch(Exception e)
				{
					System.out.println("Z2Sender.SenderThread.run: "+e);
				}
			}
			while(buffor.size() > 0);
		}

	}



	class ReceiverThread extends Thread
	{

		public void run()
		{
			try
			{
				while(true)
				{
					byte[] data=new byte[datagramSize];
					DatagramPacket packet= 
							new DatagramPacket(data, datagramSize);
					socket.receive(packet);
					Z2Packet p=new Z2Packet(packet.getData());
					
					if(p.getIntAt(0) > expectedToRead)
						expectedToRead = p.getIntAt(0);
				}
			}
			catch(Exception e)
			{
				System.out.println("Z2Sender.ReceiverThread.run: "+e);
			}
		}

	}


	public static void main(String[] args)
			throws Exception
	{
		Z2Sender sender=new Z2Sender( Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		sender.sender.start();
		sender.receiver.start();
	}



}