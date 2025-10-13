package module22;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import module22.config.HibernateUtil;
import module22.dao.HibernateUserDao;
import module22.dao.UserDao;
import module22.entity.User;


public class Main {
    private static final UserDao userDao = new HibernateUserDao();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Create user");
            System.out.println("2. Find all");
            System.out.println("3. Find by id");
            System.out.println("4. Update user");
            System.out.println("5. Delete user");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": {
                        System.out.print("Name: ");  String name = sc.nextLine();
                        System.out.print("Email: "); String email = sc.nextLine();
                        System.out.print("Age (int or blank): "); String ageStr = sc.nextLine();
                        Integer age = ageStr.isBlank() ? null : Integer.parseInt(ageStr);

                        Long id = userDao.create(new User(name, email, age));
                        System.out.println("User created successfully: id= " + id + ", email= " + email + ", age= " + age);
                        break;
                    }
                    case "2": {
                        List<User> users = userDao.findAll();
                        users.forEach(System.out::println);
                        break;
                    }
                    case "3": {
                        System.out.print("Id: "); Long fid = Long.parseLong(sc.nextLine());
                        Optional<User> found = userDao.findById(fid);
                        System.out.println(found.isPresent() ? found.get() : "User with id " + fid + " not found");
                        break;
                    }
                    case "4": {
                        System.out.print("Id to update: "); Long uid = Long.parseLong(sc.nextLine());
                        Optional<User> u = userDao.findById(uid);
                        if (u.isEmpty()) {
                            System.out.println("User with id " + uid + " not found");
                            break;
                        }
                        User toUpdate = u.get();
                        System.out.print("New name (empty entry=skip): "); String nn = sc.nextLine();
                        System.out.print("New email (empty entry=skip): "); String ne = sc.nextLine();
                        System.out.print("New age (empty entry=skip): "); String na = sc.nextLine();

                        if (!nn.isBlank()) toUpdate.setName(nn);
                        if (!ne.isBlank()) toUpdate.setEmail(ne);
                        if (!na.isBlank()) toUpdate.setAge(Integer.parseInt(na));

                        userDao.update(toUpdate);
                        System.out.println("User id " + uid + " was updated");
                        System.out.println("Name: " + toUpdate.getName() + ", Email: " + toUpdate.getEmail() + ", Age: " + toUpdate.getAge());
                        break;
                    }
                    case "5": {
                        System.out.print("Id to delete: "); 
                        Long did = Long.parseLong(sc.nextLine());
                        Optional<User> du = userDao.findById(did);
                        if (du.isPresent()) {
                            userDao.delete(du.get());
                            System.out.println("User with id " + did + " was deleted");
                        } else {
                            System.out.println("User with id " + did + " not found");
                        }
                        break;
                    }
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown option");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        HibernateUtil.shutdown();
    }
}
