package sr.serialization;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

enum PhoneTypeJ { MOBILE,  HOME,  WORK};

class PhoneNumberJ implements java.io.Serializable {
	private static final long serialVersionUID = 2463673954867473008L;
	public String number;
	public PhoneTypeJ type;

	public PhoneNumberJ(String number, PhoneTypeJ type) 
    { 
        this.number = number; 
        this.type = type; 
    } 
}


class PersonJ implements java.io.Serializable 
{ 
	private static final long serialVersionUID = 2363673954867473008L;
	public int a; 
	public String b; 

	public String name;
	public int id;
	public String email;
	public List<PhoneNumberJ> phones;

	public PersonJ(String name, int id, String email, List<PhoneNumberJ> phones) 
    { 
        this.name = name; 
        this.id = id; 
        this.email = email;
        this.phones = phones;
    } 

} 

public class JavaSerialization 
{ 
	public static void main(String[] args) 
	{    

		try
		{
			PersonJ object = null;
			long n = 1000;

			System.out.println("Performing object creation " + n + " times...");

			for(int i = 0; i < n ; i++) {
				List<PhoneNumberJ> phones = new ArrayList<PhoneNumberJ>();
				phones.add(new PhoneNumberJ("+48-12-555-4321", PhoneTypeJ.HOME));
				phones.add(new PhoneNumberJ("+48-699-989-796", PhoneTypeJ.MOBILE));

				object = new PersonJ("Kazimierz Kowalewski", 123456, "kowal@poczta.com", phones);
			}
			System.out.println("... finished.");


			System.out.println("Performing java serialization " + n + " times...");
			long start1 = System.nanoTime();
			for(long i = 0; i < n; i++)
			{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(object);
				oos.flush();
				bos.toByteArray();
			}
			System.out.println("... finished.");
			long end1 = System.nanoTime();
			System.out.println("Elapsed Time in nano seconds: "+ (end1-start1));

	        //serialize again (only once) and write to a file
			FileOutputStream file = new FileOutputStream("person1.ser"); 
			ObjectOutputStream out = new ObjectOutputStream(file); 
			out.writeObject(object); 
			out.close(); 
			file.close(); 
		} 
		catch(IOException ex) 
		{ 
			System.out.println("IOException"); 
		} 
	}
} 