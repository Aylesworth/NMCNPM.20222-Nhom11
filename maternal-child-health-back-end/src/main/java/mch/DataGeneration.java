package mch;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.gson.Gson;

import mch.model.User;
import mch.repository.UserRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Configuration
public class DataGeneration {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Bean
	public CommandLineRunner runner() {
		return (args) -> {
//			System.out.println(generateUserPassword());
		};
	}

	private String mockUserData() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"INSERT INTO user (email, password, full_name, citizen_id, dob, sex, phone_number, address, insurance_id, status) VALUES ");

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("https://random-data-api.com/api/v2/users?size=100")
				.method("GET", null).build();

		Response response = client.newCall(request).execute();

		List<Map<String, Object>> data = new Gson().fromJson(response.body().string(),
				new ArrayList<Map<String, Object>>().getClass());

		Random rd = new Random();

		data.stream().forEach(map -> {
			String email = (String) map.get("email");
			String password = passwordEncoder.encode((String) map.get("password"));
			String fullName = map.get("first_name") + " " + map.get("last_name");
			String citizenId = randomNumberSeries(12);
			String dob = (String) map.get("date_of_birth");
			String sex = rd.nextInt(0, 2) == 1 ? "Nam" : "Nữ";
			String phoneNumber = "0" + randomNumberSeries(9);
			String address = ((String) ((Map<String, String>) map.get("address")).get("street_address"));
			String insuranceId = ((char) rd.nextInt(65, 91)) + "" + ((char) rd.nextInt(65, 91))
					+ randomNumberSeries(13);
			String status = rd.nextInt(0, 2) == 1 ? "approved" : "pending";

			sb.append("('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'), \n".formatted(email, password, fullName,
					citizenId, dob, sex, phoneNumber, address, insuranceId, status));

		});

		return sb.toString();
	}

	private String generateUserPassword() {
		StringBuilder sb = new StringBuilder();
		
		List<User> users = userRepository.findAll();
		users.stream().forEach(u -> {
			String password = passwordEncoder.encode(u.getEmail().substring(0, u.getEmail().indexOf('@')));

			sb.append("UPDATE user SET password = '%s' WHERE id = %d;\n".formatted(password, u.getId()));
		});
		
		return sb.toString();
	}

	private String mockChildData() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"INSERT INTO child (full_name, nickname, dob, sex, ethnicity, nationality, birthplace, insurance_id, parent_id) VALUES ");

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("https://random-data-api.com/api/v2/users?size=100")
				.method("GET", null).build();

		Response response = client.newCall(request).execute();

		List<Map<String, Object>> data = new Gson().fromJson(response.body().string(),
				new ArrayList<Map<String, Object>>().getClass());

		response = client.newCall(request).execute();

		data.addAll(new Gson().fromJson(response.body().string(), new ArrayList<Map<String, Object>>().getClass()));

		Random rd = new Random();

		String[] nicknames = { "DJ", "Marge", "Drake", "Butter", "Gizmo", "Pookie", "Tank", "Frodo", "Janitor",
				"Butternut", "Big Nasty", "Queenie", "Biscuit", "Elf", "Ace", "Spud", "Piggy", "Ash", "Raisin",
				"Headlights", "Baby Maker", "Red", "Mimi", "Genius", "Biscuit", "Elf", "Ace", "Spud", "Piggy", "Ash",
				"Raisin", "Headlights", "Baby Maker", "Red", "Mimi", "Genius" };

		String[] birthplaces = { "Hà Nội", "Hải Phòng", "Bắc Ninh", "Hải Dương", "Hưng Yên", "Nam Định", "Thái Bình",
				"Phú Thọ", "Vĩnh Phúc", "Bắc Giang", "Quảng Ninh", "Hòa Bình", "Hà Nam", "Ninh Bình", "Thái Nguyên",
				"Lạng Sơn", "Cao Bằng", "Bắc Kạn", "Tuyên Quang", "Yên Bái", "Lào Cai", "Điện Biên", "Lai Châu",
				"Sơn La", "Hà Giang" };

		data.stream().forEach((map) -> {
			String fullName = map.get("first_name") + " " + map.get("last_name");
			String nickname = rd.nextInt(2) == 1 ? nicknames[rd.nextInt(nicknames.length)] : null;
			String dob = generateRandomDate("2018-01-01", "2023-05-26");
			String sex = rd.nextInt(2) == 1 ? "Nam" : "Nữ";
			String ethnicity = rd.nextInt(20) > 3 ? "Kinh"
					: rd.nextInt(2) == 1 ? "Tày" : rd.nextInt(2) == 1 ? "Mường" : rd.nextInt(2) == 1 ? "Thái" : "Khmer";
			String nationality = rd.nextInt(10) > 0 ? "Việt Nam"
					: rd.nextInt(2) == 1 ? "Đức"
							: rd.nextInt(2) == 1 ? "Hoa Kỳ"
									: rd.nextInt(2) == 1 ? "Nhật Bản"
											: rd.nextInt(2) == 1 ? "Nga"
													: rd.nextInt(2) == 1 ? "CH Séc" : "Tây Ban Nha";
			String birthplace = birthplaces[rd.nextInt(birthplaces.length)];
			String insuranceId = ((char) rd.nextInt(65, 91)) + "" + ((char) rd.nextInt(65, 91))
					+ randomNumberSeries(13);
			String parentId = rd.nextInt(8, 109) + "";

			sb.append("('%s',%s,'%s','%s','%s','%s','%s','%s',%s), \n".formatted(fullName,
					nickname == null ? "null" : "'" + nickname + "'", dob, sex, ethnicity, nationality, birthplace,
					insuranceId, parentId));

		});

		return sb.toString();
	}

	private String mockBodyMetricsData() {
		Random rd = new Random();

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO body_metrics (child_id, height, weight, measurement_date, note) VALUES\n");

		for (int i = 1; i <= 202; i++) {
			double height = rd.nextDouble(40, 100);
			double weight = rd.nextDouble(10, 30);
			long epochDay = LocalDate.of(2023, 1, 1).toEpochDay();

			for (int j = 0; j <= rd.nextInt(10); j++) {
				height += rd.nextDouble(5);
				weight += rd.nextDouble(2);
				epochDay += 30 + rd.nextInt(-10, 11);
				sb.append("(%d, %.2f, %.2f, '%s', null), \n".formatted(i, height, weight,
						LocalDate.ofEpochDay(epochDay).toString()));
			}
		}

		return sb.toString();
	}

	private String generateRandomDate(String start, String end) {
		LocalDate startDate = LocalDate.parse(start);
		LocalDate endDate = LocalDate.parse(end);
		long startEpochDay = startDate.toEpochDay();
		long endEpochDay = endDate.toEpochDay();

		// Generate a random number of days between the start and end dates
		long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);

		// Convert the random number of days back to a LocalDate
		return LocalDate.ofEpochDay(randomDay).toString();
	}

	private String randomNumberSeries(int length) {
		StringBuilder sb = new StringBuilder();
		Random rd = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(rd.nextInt(0, 10));
		}
		return sb.toString();
	}
}
