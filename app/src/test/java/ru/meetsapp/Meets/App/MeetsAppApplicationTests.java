package ru.meetsapp.Meets.App;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.meetsapp.Meets.App.dto.BioDTO;
import ru.meetsapp.Meets.App.dto.CommentDTO;
import ru.meetsapp.Meets.App.dto.MeetDTO;
import ru.meetsapp.Meets.App.dto.UserDTO;
import ru.meetsapp.Meets.App.entity.Bio;
import ru.meetsapp.Meets.App.entity.Comment;
import ru.meetsapp.Meets.App.entity.Meet;
import ru.meetsapp.Meets.App.entity.User;
import ru.meetsapp.Meets.App.services.CommentService;
import ru.meetsapp.Meets.App.services.MeetService;
import ru.meetsapp.Meets.App.services.UserService;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class WordGenerator{
	static String generateWord(int size){
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			int randomLimitedInt = leftLimit + (int)
					(random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}

		return buffer.toString();
	}
}

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest{
	@Autowired
	UserService userService;

	@Test
	public void beforeAll(){
		try{
			UserDTO userDTO = new UserDTO();
			userDTO.email = "nselyavin@inbox.ru";
			userDTO.username = "Oleg321";
			userDTO.password = "1324";
			userDTO.name = "Vasya";
			userDTO.lastname = "Grishin";
			userDTO.birthDay = "2001-12-28";
			userService.createUser(userDTO);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}


	@Test
	void createUser(){

		UserDTO userDTO = new UserDTO();
		userDTO.username = WordGenerator.generateWord(10);
		userDTO.name = "Vasya";
		userDTO.email = "nselyavin@inbox.ru";
		userDTO.lastname = "Grishin";
		userDTO.birthDay = "2001-12-28";
		userDTO.password = WordGenerator.generateWord(10);

		assertDoesNotThrow(() -> {
			User user = userService.createUser(userDTO);
			assertEquals(user.getUsername(), userService.getUserById(user.getId()).getUsername());
			// Second user with same username
			assertThrows(RuntimeException.class, ()->{
				userService.createUser(userDTO);
			});

			userService.deleteUserById(user.getId());
		});
	}

// @Email перенесен в ДТО, тест сейчас не имеет смысла
//	@Test
//	void createUserWithBadMail(){
//
//		UserDTO userDTO = new UserDTO();
//		userDTO.username = WordGenerator.generateWord(10);
//		userDTO.name = "Vasya";
//		userDTO.email = "badmail";
//		userDTO.lastname = "Grishin";
//		userDTO.password = WordGenerator.generateWord(10);
//
//		// Second user with same username
//		assertThrows(RuntimeException.class, ()->{
//			User user = userService.createUser(userDTO);
//		});
//	}

	@Test
	void bookmarkTest(){
		UserDTO userDTO = new UserDTO();
		userDTO.username = WordGenerator.generateWord(10);
		userDTO.name = "Vasya";
		userDTO.email = "nselyavin@inbox.ru";
		userDTO.birthDay = "2001-12-28";
		userDTO.lastname = "Grishin";
		userDTO.password = WordGenerator.generateWord(10);
		User bookmark = userService.createUser(userDTO);

		assertDoesNotThrow(() -> {
			User user = userService.getUserByUsername("Vasya228");
			userService.bookmarkUser(user.getId(), bookmark.getId());
			Set<Long> bookmarks = userService.getBookmarksIdById(user.getId());
			assertTrue(bookmarks.contains(bookmark.getId()));

			userService.deleteUserById(bookmark.getId());
		});
	}

	@Test
	void likeTest(){
		UserDTO userDTO = new UserDTO();
		userDTO.username = WordGenerator.generateWord(10);
		userDTO.name = "Vasya";
		userDTO.email = "nselyavin@inbox.ru";
		userDTO.lastname = "Grishin";
		userDTO.birthDay = "2001-12-28";
		userDTO.password = WordGenerator.generateWord(10);
		User likedUser = userService.createUser(userDTO);

		assertDoesNotThrow(() -> {
			User user = userService.getUserByUsername("Vasya228");
			userService.likeUser(user.getId(), likedUser.getId());
			Set<Long> likedUsers = userService.getLikedUsersIdById(user.getId());
			assertTrue(likedUsers.contains(likedUser.getId()));

			userService.deleteUserById(likedUser.getId());
		});
	}

	@Test
	void bioUpdateTest(){
		UserDTO userDTO = new UserDTO();
		userDTO.username = WordGenerator.generateWord(10);
		userDTO.name = "Vasya";
		userDTO.email = "nselyavin@inbox.ru";
		userDTO.lastname = "Grishin";
		userDTO.birthDay = "2001-12-28";
		userDTO.password = WordGenerator.generateWord(10);

		assertDoesNotThrow(() -> {
			User user = userService.createUser(userDTO);
			BioDTO bioDTO = new BioDTO();
			bioDTO.biography = "Poz";
			bioDTO.gender = "male";
			bioDTO.hairColor = "green";
			bioDTO.height = 172.0f;
			bioDTO.weight = 80.0f;
			bioDTO.specialSigns = "Monstr";
			user = userService.updateBio(user.getUsername(), bioDTO);
			assertEquals(user.getBio().getGender(), "male");
			assertEquals(user.getBio().getHeight(), bioDTO.height);

			userService.deleteUserById(user.getId());
		});
	}
}

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MeetServiceTest{
	@Autowired
	MeetService meetService;
	@Autowired
	UserService userService;

	@BeforeEach
	public void beforeAll(){
		try{
			UserDTO userDTO = new UserDTO();
			userDTO.email = "nselyavin@inbox.ru";
			userDTO.username = "Vasya228";
			userDTO.password = "1324";
			userDTO.name = "Vasya";
			userDTO.lastname = "Grishin";
			userDTO.birthDay = "2001-12-28";
			userService.createUser(userDTO);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	void createMeetTest(){
		User user = userService.getUserByUsername("Vasya228");
		MeetDTO meetDTO = new MeetDTO();
		meetDTO.creator = user.getUsername();
		meetDTO.meetDate = "2020-11-12";
		meetDTO.meetTime = "12:23";
		meetDTO.title = WordGenerator.generateWord(12);
		meetDTO.location = "Moskva, Lenina, 3";

		assertDoesNotThrow(()->{
			Meet meet = meetService.createMeet(meetDTO);
			assertEquals("2020-11-12", meet.getSDate());
			assertEquals("12:23", meet.getSTime());
			meetService.deleteMeetById(meet.getId());
		});
	}

	@Test
	void addUserToMeet(){
		User user = userService.getUserByUsername("Vasya228");
		MeetDTO meetDTO = new MeetDTO();
		meetDTO.creator = user.getUsername();
		meetDTO.meetDate = "2020-11-12";
		meetDTO.meetTime = "12:23";
		meetDTO.title = WordGenerator.generateWord(12);
		meetDTO.location = "Moskva, Lenina, 3";
		UserDTO userDTO = new UserDTO();
		userDTO.name = "Leha";
		userDTO.lastname = "Ivanova";
		userDTO.username = WordGenerator.generateWord(10);
		userDTO.email = "goodmail@mail.ru";
		userDTO.birthDay = "2001-12-28";
		userDTO.password = "pass";

		assertDoesNotThrow(()->{
			Meet meet = meetService.createMeet(meetDTO);
			User addedUser = userService.createUser(userDTO);
			meet = meetService.addUserToMeet(meet.getId(), addedUser.getUsername());
			assertTrue(meet.getMeetUsers().contains(addedUser.getId()));

			meetService.deleteMeetById(meet.getId());
			userService.deleteUserById(addedUser.getId());
		});
	}
}

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceTest{
	@Autowired
	MeetService meetService;
	@Autowired
	CommentService commentService;
	@Autowired
	UserService userService;

	Meet meet;

	@BeforeEach
	public void setUpFixture(){

		try{
			User user;
			try {
				user = userService.getUserByUsername("Vasya228");
			} catch (Exception e) {
				UserDTO userDTO = new UserDTO();
				userDTO.email = "nselyavin@inbox.ru";
				userDTO.username = "Vasya228";
				userDTO.password = "1324";
				userDTO.name = "Vasya";
				userDTO.lastname = "Grishin";
				userDTO.birthDay = "2001-12-28";
				user = userService.createUser(userDTO);
			}

			MeetDTO meetDTO = new MeetDTO();
			meetDTO.creator = user.getUsername();
			meetDTO.meetDate = "2020-11-12";
			meetDTO.meetTime = "12:23";
			meetDTO.title = WordGenerator.generateWord(12);
			meetDTO.location = "Moskva, Lenina, 3";
			meet = meetService.createMeet(meetDTO);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void createCommentTest(){
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.meetId = meet.getId();
		commentDTO.username = WordGenerator.generateWord(10);
		commentDTO.message = "Message";
		commentDTO.userId = 2L;

		assertDoesNotThrow(()->{
			Comment comment = commentService.addComment(commentDTO);

			commentService.deleteComment(comment.getId());

		});
	}

	@AfterEach
	public void clearFixture(){

		meetService.deleteMeetById(meet.getId());
	}

}
