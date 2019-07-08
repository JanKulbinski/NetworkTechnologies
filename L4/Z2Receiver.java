import java.net.*;

public class Z2Receiver
{
	static final int interval = 10;
	static final int datagramSize = 50;
	InetAddress localHost;
	int destinationPort;
	DatagramSocket socket;
	ReceiverThread receiver;
	
	boolean[] wasRead;
	char[] buffor; 
	int expectedToRead = 0;
	
	public Z2Receiver(int myPort, int destPort)
			throws Exception
	{
		localHost=InetAddress.getByName("127.0.0.1");
		destinationPort=destPort;
		socket=new DatagramSocket(myPort);
		receiver=new ReceiverThread();    
		buffor = new char[datagramSize];
		wasRead = new boolean[datagramSize];
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
					
					if(!wasRead[p.getIntAt(0)]) {
						buffor[p.getIntAt(0)] = (char) p.data[4];
						wasRead[p.getIntAt(0)] = true;
						
						if(p.getIntAt(0) == expectedToRead) {
						
							int j = expectedToRead;
							
							while(wasRead[j]) {
								System.out.println(buffor[j]);
								j++;
								expectedToRead++;
							}
						}
					}

					Z2Packet pToSend=new Z2Packet(4+1); 
					pToSend.setIntAt(expectedToRead,0);
					DatagramPacket packetToSend = 
							new DatagramPacket(pToSend.data, pToSend.data.length, 
									localHost, destinationPort);
					socket.send(packetToSend);
					}
			}
			
			catch(Exception e)
			{
				System.out.println("Z2Receiver.ReceiverThread.run: "+e);
			}
		}

	}

	public static void main(String[] args)
			throws Exception
	{
		Z2Receiver receiver=new Z2Receiver( Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		receiver.receiver.start();
	}


}
