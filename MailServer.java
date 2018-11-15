import java.util.LinkedList;
import java.util.TreeSet;

public class MailServer extends LinkedList<Message> {
    private TreeSet<Actor> subscribers = new TreeSet<Actor>();


    public void signUp(Actor actor){
        subscribers.add(actor);
    }

    public void dispatch(Message msg){
        if (msg.getRecipient().equals(null)) {
            for (Actor a : subscribers){
                if(! a.equals(msg.getRecipient())){
                    a.receive(msg);
                }

            }
        }

        else {
            msg.getRecipient().receive(msg);
        }
    }

}
