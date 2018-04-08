package pers.hawk.a;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import pers.hawk.room.redis.Publisher;
import pers.hawk.room.redis.Subscriber;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {

		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

		Publisher publisher = new Publisher();
		executorService.execute(publisher);

		Subscriber subscriber = new Subscriber();
		executorService.execute(subscriber);

	}
}
