import java.util.concurrent.Semaphore;

public class store {
	public static register[] register = new register[3];
	public static Semaphore outsideLine = new Semaphore(6,true); 
	public static Semaphore openStore = new Semaphore(0,true); 
	public static int inStore = 0;
	public static Semaphore inStore_mutex = new Semaphore(1,true); 
	public static final int store_cap = 6;
	public static int currentCustomers = 0;
	public static int customersServed = 0;
	public static Semaphore customersServed_mutex = new Semaphore(1,true); 
	public static int ID = 1;
	public static boolean isStoreOpen = false;
	public static Semaphore isStoreOpen_mutex = new Semaphore(1,true); 
	public static Semaphore reg1 = new Semaphore(0,true); 
	public static Semaphore reg2 = new Semaphore(0,true); 
	public static Semaphore reg3 = new Semaphore(0,true); 
	public static Semaphore reg1isBusy_mutex = new Semaphore(1,true); 
	public static Semaphore reg2isBusy_mutex = new Semaphore(1,true); 
	public static Semaphore reg3isBusy_mutex = new Semaphore(1,true);
	public static Semaphore reg1pay = new Semaphore(1,true);
	public static Semaphore reg2pay = new Semaphore(1,true);
	public static Semaphore reg3pay = new Semaphore(1,true);
	public static Semaphore registerAvailable = new Semaphore(2,true); 
	public static Semaphore elderlyRegisterAvailable = new Semaphore(2,true);
	static int maxCustomers;
	public static customer[] parkingLotLine;
	public static Semaphore[] parkingLotLineSemaphores;
	public static long time;
	
	public static void main(String[] args) {
		 maxCustomers = Integer.parseInt(args[0]);
		 parkingLotLine = new customer[maxCustomers];
		 parkingLotLineSemaphores = new Semaphore[maxCustomers];
		 time = System.currentTimeMillis();
		//initiates the semaphore array for parking lot
		for(int i = 0; i < maxCustomers; i++) {
			parkingLotLineSemaphores[i] = new Semaphore(0, true);
		}
		manager man = new manager();
		man.start();
		for(int i = 0; i <3; i++) {             //starts the register threads, 1 and 2 are regular and register 3 is for the elderly
			register[i] = new register(Integer.toString(i+1));
			register[i].start();
		}
		employee emp = new employee();
		emp.start();
		customer cust;
		int toOpenStore = (int)(Math.random() * (10 - 6)) + 6;
		while (currentCustomers < maxCustomers) {
            //customers arrive every 1-10 seconds and join the line outside 
            try {
				Thread.sleep((int)(Math.random() * (10000 - 1000)) + 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
            if ((int)(Math.random() * (4 - 1)) + 1 == 1) {
                cust = new customer(Integer.toString(ID), true);
            } else {
                cust = new customer(Integer.toString(ID), false);
            }
            cust.start();
            ID++;
            currentCustomers++;
            if(currentCustomers == toOpenStore) {
            	openStore.release();
            	isStoreOpen = true;
            }
        }
	}

}
