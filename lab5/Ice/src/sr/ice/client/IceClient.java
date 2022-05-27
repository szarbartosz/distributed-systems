package sr.ice.client;
// **********************************************************************
//
// Copyright (c) 2003-2019 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

import Demo.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.LocalException;

public class IceClient 
{
	public static void main(String[] args) 
	{
		int status = 0;
		Communicator communicator = null;

		try {
			// 1. Inicjalizacja ICE
			communicator = Util.initialize(args);

			// 2. Uzyskanie referencji obiektu na podstawie linii w pliku konfiguracyjnym (w�wczas aplikacj� nale�y uruchomi� z argumentem --Ice.config=config.client)
			//ObjectPrx base1 = communicator.propertyToProxy("Calc1.Proxy");
			
			// 2. To samo co powy�ej, ale mniej �adnie
			ObjectPrx base1 = communicator.stringToProxy("calc/calc11:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z"); //opcja -z w��za **mo�liwo��** kompresji
			ObjectPrx base2 = communicator.stringToProxy("calc/calc33:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z");

			// 3. Rzutowanie, zaw�anie (do typu Calc)
			CalcPrx obj1 = CalcPrx.checkedCast(base1);
			if (obj1 == null) throw new Error("Invalid proxy");

			CalcPrx obj2 = CalcPrx.checkedCast(base2);
			if (obj2 == null) throw new Error("Invalid proxy");
			
			CompletableFuture<Long> cfl = null;
			String line = null;
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

			// 4. Wywo�anie zdalnych operacji i zmiana trybu proxy dla zmiennej obj1
			do	{
				try	{
					System.out.println("==> ");
					line = in.readLine();
					
					if (line == null)
					{
						break;
					}
					else if (line.equals("add"))
					{
						long r = obj1.add(7, 8);
						System.out.println("RESULT = " + r);
						long r2 = obj2.add(7, 8);
						System.out.println("RESULT = " + r2);
					}
					else if (line.equals("add2")) 
					{
						long r = obj1.add(7000, 8000);
						System.out.println("RESULT = " + r);
					}
					else if (line.equals("subtract"))
					{
						long r = obj1.subtract(7, 8);
						System.out.println("RESULT = " + r);
					}
					else if (line.equals("op"))
					{
						A a = new A((short)11, 22, 33.0f, "ala ma kota");
						obj1.op(a, (short)44);
						System.out.println("DONE");
					} 
					else if (line.equals("op2"))
					{
						A a = new A((short)11, 22, 33.0f, "ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ala ma kota ");
						obj1.op(a, (short)44);
						System.out.println("DONE");
					} 
					else if (line.equals("op 10"))
					{
						A a = new A((short)11, 22, 33.0f, "ala ma kota");
						for(int i = 0; i < 10; i++) obj1.op(a, (short)44);
						System.out.println("DONE");
					}
					else if (line.equals("add-with-ctx")) //wys�anie dodatkowych danych stanowi�cych kontekst wywo�ania
					{
						Map<String,String> map = new HashMap<String, String>();
						map.put("key1", "val1");
						map.put("key2", "val2");
						long r = obj1.add(7, 8, map);
						System.out.println("RESULT = " + r);
					}
					else if (line.equals("compress on"))  
					{
						obj1.ice_compress(true);
						System.out.println("Compression enabled");
					} 
					else if (line.equals("compress off"))  
					{
						obj1.ice_compress(false);
						System.out.println("Compression disabled");
					} 

					/* PONI�EJ WYWO�ANIA REALIZOWANE W TRYBIE ASYNCHRONICZNYM */
					
					else if (line.equals("add-asyn1")) //future-based
					{
						obj1.addAsync(7000, 8000).whenComplete((result, ex) -> 
							{
								System.out.println("RESULT (asyn) = " + result);
							}
						);
					}
					else if (line.equals("add-asyn2-req")) //future-based  1. zlecenie wywo�ania
					{
						cfl = obj1.addAsync(7000, 8000);						
					}
					else if (line.equals("add-asyn2-res")) //future-based  2. odebranie wyniku
					{
						long r = cfl.join();						
						System.out.println("RESULT = " + r);
					}
					else if (line.equals("op-asyn1a 100")) //co si� dzieje "w sieci"?
					{
						A a = new A((short)11, 22, 33.0f, "ala ma kota");
						for(int i = 0; i <100; i++) 
						{
							obj1.opAsync(a, (short)99);
						}
						System.out.println("DONE");
					}
					else if (line.equals("op-asyn1b 100")) 
					{
						A a = new A((short)11, 22, 33.0f, "ala ma kota");
						for(int i = 0; i <100; i++) 
						{
							obj1.opAsync(a, (short)99).whenComplete((result, ex) -> 
								{
									System.out.println("CALL (asyn) finished");
								}
							);
						}
						System.out.println("DONE");
					}
					
					/* PONI�EJ USTAWIANIE TRYBY PRACY PROXY */
					
					else if (line.equals("set-proxy twoway")) 
					{
						obj1 = obj1.ice_twoway();
						System.out.println("obj1 proxy set to 'twoway' mode");
					}
					else if (line.equals("set-proxy oneway")) 
					{
						obj1 = obj1.ice_oneway();
						System.out.println("obj1 proxy set to 'oneway' mode");
					}
					else if (line.equals("set-proxy datagram")) 
					{
						obj1 = obj1.ice_datagram();
						System.out.println("obj1 proxy set to 'datagram' mode");
					}
					else if (line.equals("set-proxy batch oneway")) 
					{
						obj1 = obj1.ice_batchOneway();
						System.out.println("obj1 proxy set to 'batch oneway' mode");
					}
					else if (line.equals("set-proxy batch datagram")) 
					{
						obj1 = obj1.ice_batchDatagram();
						System.out.println("obj1 proxy set to 'batch datagram' mode");
					}
					else if (line.equals("flush")) //sensowne tylko dla operacji wywo�ywanych w trybie batch
					{
						obj1.ice_flushBatchRequests();
						System.out.println("Flush DONE");
					} 
					else if (line.equals("x")) // exit
					{
					}
					else
					{
						System.out.println("???");
					}
				}
				catch (java.io.IOException ex)
				{
					System.err.println(ex);
				}
				catch (com.zeroc.Ice.TwowayOnlyException ex)
				{
					System.err.println(ex);
				}
			}
			while (!line.equals("x"));


		} catch (LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			status = 1;
		}
		if (communicator != null) { //clean
			try {
				communicator.destroy();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				status = 1;
			}
		}
		System.exit(status);
	}

}