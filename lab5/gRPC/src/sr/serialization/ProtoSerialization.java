package sr.serialization;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import sr.proto.AddressBookProtos.Person;

public class ProtoSerialization {

	public static void main(String[] args)
	{
		try {
			new ProtoSerialization().testProto();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testProto() throws IOException
	{
		long n = 1000;
		Person p1 = null;

		System.out.println("Performing object creation " + n + " times...");
		for(long i = 0; i < n; i++) {
			p1 =
					Person.newBuilder()
							.setId(123456)
							.setName("Kazimierz Kowalewski")
							.setEmail("kowal@poczta.com")
							.addPhones(
									Person.PhoneNumber.newBuilder()
											.setNumber("+48-12-555-4321")
											.setType(Person.PhoneType.HOME))
							.addPhones(
									Person.PhoneNumber.newBuilder()
											.setNumber("+48-699-989-796")
											.setType(Person.PhoneType.MOBILE))
							.build();
		}
		System.out.println("... finished.");

		byte[] p1ser = null;

        System.out.println("Performing proto serialization " + n + " times...");
		long start1 = System.nanoTime();
        for(long i = 0; i < n; i++)
		{
			p1ser = p1.toByteArray();
		}
        System.out.println("... finished.");
		long end1 = System.nanoTime();
		System.out.println("Elapsed Time in nano seconds: "+ (end1-start1));

		System.out.println(new String(p1ser, StandardCharsets.UTF_8));
		System.out.println(p1ser.length + " " + Arrays.toString(p1ser));
		StringBuilder hex = new StringBuilder();
		for (byte aByte : p1ser) hex.append(String.format("%02x", aByte));
		System.out.println(hex);
		//System.out.println(new BigInteger(1, p1ser).toString(16));


		//serialize again (only once) and write to a file
		FileOutputStream file = new FileOutputStream("person2.ser"); 
		file.write(p1.toByteArray()); 
		file.close();


		//Person p2 = Person.parseFrom(p1ser);
		//System.out.println(p2.getName());


	}	
}
