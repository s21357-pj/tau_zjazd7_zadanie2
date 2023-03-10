import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.example.FriendsCollection;
import org.example.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.example.FriendshipsMongo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.List;

@ExtendWith(EasyMockExtension.class)
public class FriendshipsMongoEasyMockTest {

	@TestSubject
	FriendshipsMongo friendships = new FriendshipsMongo();

	//A nice mock expects recorded calls in any order and returning null for other calls
	@Mock(type = MockType.NICE)
	FriendsCollection friends;

	@Test
	public void mockingWorksAsExpected(){
		Person joe = new Person("Joe");
		//Zapisanie zachowania - co sie ma stac
		expect(friends.findByName("Joe")).andReturn(joe);
		//Odpalenie obiektu do sprawdzenia zachowania
		replay(friends);
		assertThat(friends.findByName("Joe")).isEqualTo(joe);
	}

	@Test
	public void alexDoesNotHaveFriends(){
		assertThat(friendships.getFriendsList("Alex")).isEmpty();
	}

	@Test
	public void alexKateNotFriends(){
		assertThat(friendships.areFriends("Alex", "Kate")).isFalse();
	}


	@Test
	public void joeHas5Friends(){
		List<String> expected = Arrays.asList(new String[]{"Karol","Dawid","Maciej","Tomek","Adam"});
		Person joe = createMock(Person.class);
		expect(friends.findByName("Joe")).andReturn(joe);
		expect(joe.getFriends()).andReturn(expected);
		replay(friends);
		replay(joe);
		assertThat(friendships.getFriendsList("Joe")).hasSize(5).containsOnly("Karol","Dawid","Maciej","Tomek","Adam");
	}

	@Test
	public void joeFriendKate(){
		Person kate = createMock(Person.class);
		Person joe = createMock(Person.class);
		expect(friends.findByName("Joe")).andReturn(joe);
		expect(friends.findByName("Kate")).andReturn(kate);
		expect(joe.getFriends()).andReturn(Arrays.asList(new String[]{ "Kate" }));
		expect(kate.getFriends()).andReturn(Arrays.asList(new String[]{ "Joe" }));
		replay(friends);
		replay(joe);
		replay(kate);
		assertThat(friendships.areFriends("Kate", "Joe")).isTrue();
	}

	@Test
	public void joeNotFriendKate(){
		Person kate = createMock(Person.class);
		Person joe = createMock(Person.class);
		expect(friends.findByName("Joe")).andReturn(joe);
		expect(friends.findByName("Kate")).andReturn(kate);
		expect(joe.getFriends()).andReturn(Arrays.asList(new String[]{ "" }));
		expect(kate.getFriends()).andReturn(Arrays.asList(new String[]{ "" }));
		replay(friends);
		replay(joe);
		replay(kate);
		assertThat(friendships.areFriends("Kate", "Joe")).isFalse();
	}

}