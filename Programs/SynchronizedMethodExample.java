class TicketBooking {

    int availableTickets = 5;

    synchronized void bookTicket(String name, int tickets) {

        System.out.println(name + " is trying to book " + tickets + " tickets.");

        if (availableTickets >= tickets) {
            System.out.println("Tickets available for " + name);

            availableTickets = availableTickets - tickets;

            System.out.println(name + " successfully booked "
                               + tickets + " tickets.");

            System.out.println("Remaining tickets: " + availableTickets);
        } 
        else {
            System.out.println("Sorry " + name
                               + ", tickets are not available.");
        }
    }
}

public class SynchronizedMethodExample {

    public static void main(String[] args) {

        TicketBooking booking = new TicketBooking();

        Thread t1 = new Thread(() -> {
            booking.bookTicket("Alice", 3);
        });

        Thread t2 = new Thread(() -> {
            booking.bookTicket("Bob", 3);
        });

        t1.start();
        t2.start();
    }
}