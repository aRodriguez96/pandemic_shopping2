public class customer implements Runnable {

	public boolean isElderly;
    public String ID;
    public Thread thread = Thread.currentThread();;

    public customer(String ID, Boolean isElderly) {
    	this.isElderly = isElderly;
    	this.ID = ID;
        this.thread = new Thread(this);

    }

    public void start() {
        thread.start();
    }

    public void run() {
    	System.out.println("Customer-"+ID+" got on the line outside and elderly is "+isElderly);
    	try {
			store.outsideLine.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println("Customer-"+ID+" enters the store and begins shopping");
    	try {
            Thread.sleep((long)(Math.random() * (20000 - 10000)) + 10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
  
    	System.out.println("Customer-"+ID+" gets on the checkoutLine");
    	try {
    		if(isElderly == true) {
    			store.elderlyRegisterAvailable.acquire();
    		}
    		else {
    			store.registerAvailable.acquire();
    		}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	try{
    		if(isElderly == true) {
    			store.reg3isBusy_mutex.acquire();
	    		store.register[2].isBusy = true;
	    	store.reg3isBusy_mutex.release();
	    		//store.register[0].customerServing = ID;
	    	System.out.println("Register-3 serves Customer-"+ID);
	    		store.reg3.release();
	    		store.reg3pay.acquire();
	    		store.reg3pay.release();
	    	store.reg3isBusy_mutex.acquire();
	    		store.register[2].isBusy = false;
	    	store.reg3isBusy_mutex.release();
    		}
    		else if(store.register[0].isBusy == false){
	    		store.reg1isBusy_mutex.acquire();
		    		store.register[0].isBusy = true;
		    	store.reg1isBusy_mutex.release();
		    		//store.register[0].customerServing = ID;
		    	System.out.println("Register-1 serves Customer-"+ID);
		    		store.reg1.release();
		    		store.reg1pay.acquire();
		    		store.reg1pay.release();
		    	store.reg1isBusy_mutex.acquire();
		    		store.register[0].isBusy = false;
		    	store.reg1isBusy_mutex.release();
    	   } else if(store.register[1].isBusy == false){
	    		store.reg2isBusy_mutex.acquire();
		    		store.register[1].isBusy = true;
		    	store.reg2isBusy_mutex.release();
		    		//store.register[1].customerServing = ID;
		    	System.out.println("Register-2 serves Customer-"+ID);
		    		store.reg2.release();
		    		store.reg2pay.acquire();
		    		store.reg2pay.release();
		    	store.reg2isBusy_mutex.acquire();
		    		store.register[1].isBusy = false;
		    	store.reg2isBusy_mutex.release();
    	   }
    	}  catch (InterruptedException e2) {
			e2.printStackTrace();
		}
    	//CUSOMTER LEAVES
    	try {
			store.customersServed_mutex.acquire();
			store.customersServed++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	store.customersServed_mutex.release();
    	try {
			store.inStore_mutex.acquire();
			store.inStore--;
		 	System.out.println("Customer-"+ID+" has paid and leaves the store");
		 	store.parkingLotLine[Integer.parseInt(ID)-1] = this;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	store.inStore_mutex.release();
    	try {
			store.parkingLotLineSemaphores[Integer.parseInt(ID)-1].acquire();
			System.out.println("Customer-"+ID+" leaves the parking lot");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}