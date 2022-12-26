import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FutureBuilder {
    static Scanner sc = new Scanner(System.in);
    static SimpleDateFormat glock = new SimpleDateFormat("dd MMMM YYYY, HH:mm");

    public static class Student {
        private ArrayList<Company> company_registered = new ArrayList<Company>();
        private String Name;
        private int Rollno;
        private double cgpa;
        private String branch;
        private Date when;
        private Company offer = null;
        private int Status = 0; // 0 unoffered | 1 offered | -1 blocked
        private Company cmp = null;

        public double getoffr() {
            if (this.offer == null)
                return 0;
            else
                return this.offer.getSal();
        }

        public void setoffr(Company salut) {
            this.offer = salut;
        }

        private void setCmp(Company compy) {
            this.cmp = compy;
        }

        public Company getCmp() {
            return this.cmp;
        }

        public void setStatus(int num) {
            this.Status = num;
        }

        public int getStatus() {
            return this.Status;
        }

        public String getName() {
            return this.Name;
        }

        public int getRoll() {
            return this.Rollno;
        }

        public void setcg() {
            System.out.println("Enter old cg :");
            double ocg = sc.nextDouble();
            sc.nextLine();
            if (ocg == this.cgpa) {
                System.out.println("Enter new cg :");
                double ncg = sc.nextDouble();
                sc.nextLine();
                this.cgpa = ncg;
                System.out.println("CGPA updated");
            }
        }

        public double getctc() {
            if (this.cmp == null)
                return 0;
            else
                return this.cmp.getSal();
        }

        public void apply_std() {
            System.out.print("Enter Name    :");
            this.Name = sc.nextLine();
            System.out.print("Enter Roll No.:");
            this.Rollno = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter CGPA    :");
            this.cgpa = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Branch  :");
            this.branch = sc.nextLine();
            PlacementCell.addstd(this);
        }

        public static void register_std(String name, int roll) { // student portal registration
            for (int i = 0; i < PlacementCell.sizestd(); i++) {
                if (PlacementCell.getstd(i).getName().equals(name) && PlacementCell.getstd(i).getRoll() == roll) {
                    Student temp = PlacementCell.getstd(i);
                    PlacementCell.stdadd(temp);
                    temp.when = new Date();
                    System.out.println("Registered");
                    System.out.println(glock.format(temp.when));
                }
            }
        }

        public void getinfo() {// display student info
            System.out.println("Name    :" + this.Name);
            System.out.println("Roll no.:" + this.Rollno);
            System.out.println("Email id:" + this.Name + this.Rollno + "@" + "iiitd.ac.in");
            System.out.println("CGPA    :" + this.cgpa);
            System.out.println("Registered on ->" + glock.format(this.when));
        }

        public void cmp_register(Company comp) { // register of company cmp
            if (this.cgpa >= comp.getcg() && this.Status == 0
                    && (this.cmp == null || comp.getSal() >= 3 * this.cmp.getSal())) {
                comp.addStd(this);
                System.out.println("Registration Successuful!\nCompany Details:");
                comp.getinfo();
            } else if (this.offer != null)
                System.out.println("Please Clear exsisting offers");
            else
                System.out.println("Not eligible for Registration");
        }

        public void application_status(Company compy) { // check if applied to company or not
            String temp = compy.getName();
            for (int i = 0; i < this.company_registered.size(); i++) {
                if (temp.equals(company_registered.get(i).getName())) {
                    System.out.println("Applied to " + temp);
                    return;
                }
            }
            System.out.println("not-applied to " + temp);
            return;
        }

        public void companyCat() { // all companies available or unavilable
            for (int i = 0; i < PlacementCell.cmpsize(); i++) {
                Company itr = PlacementCell.cmpget(i);
                itr.getinfo();
                if (this.cgpa >= itr.getcg() && this.Status != -1 && this.offer == null
                        && (this.cmp == null || itr.getSal() >= 3 * this.cmp.getSal())) {
                    System.out.println("Available");
                } else
                    System.out.println("Unavailable");
            }
        }

        public void accept() { // accept best of available offers from company (status set to 0 as offer
                               // accepted by the student)
            if (this.offer != null) {
                System.out.println("Offer from " + this.offer.Name);
                System.out.println("Enter 1 to accept or 0 to reject the offer");
                int accept = sc.nextInt();
                sc.nextLine();
                if (accept == 1) {
                    this.Status = 0;
                    this.setCmp(this.offer);
                    this.offer = null;
                    System.out.println();
                    System.out.println("offer accepted !");
                } else if (this.cmp == null) {
                    this.Status = -1;
                    for (int i = 0; i < this.company_registered.size(); i++) {
                        (company_registered.get(i)).remstd(this);
                        System.out.println("You have been BLOCKED!");
                    }
                    return;
                } else {
                    System.out.println();
                    System.out.println("offer rejected !");
                }
            } else {
                System.out.println("No new offers");
                return;
            }
        }

        public void showStatus() {
            if (this.Status == -1) {
                System.out.println();
                System.out.println("BLOCKED");
            } else if (this.Status == 1) {
                System.out.println();
                System.out.println("Latest offer from :");
                this.offer.getinfo();
                if (this.cmp != null) {
                    System.out.println();
                    System.out.println("Current placement :");
                    this.cmp.getinfo();
                }
            } else {
                if (this.cmp != null) {
                    System.out.println();
                    System.out.println("Current placement :");
                    this.cmp.getinfo();
                    return;
                }
                System.out.println();
                System.out.println("No offers yet");
                System.out.println();

            }
        }

    }

    public static class Company {
        private ArrayList<Student> registered_students = new ArrayList<Student>();
        private ArrayList<Student> selected_students = new ArrayList<Student>();
        private String Name;
        private String Role;
        private double salary;
        private double cg;
        private Date when;

        public double getcg() {
            return this.cg;
        }

        public double getSal() {
            return this.salary;
        }

        public void addStd(Student std) {
            registered_students.add(std);
            return;
        }

        public String getName() {
            return this.Name;
        }

        public void setRole() {
            System.out.print("Enter updated Role :");
            this.Role = sc.nextLine();
        }

        public void setCGPA() {
            System.out.print("Enter updated CG criteria :");
            this.cg = sc.nextDouble();
            sc.nextLine();
        }

        public void setPackage() {
            System.out.print("Enter updated Package :");
            this.salary = sc.nextDouble();
            sc.nextLine();
        }

        public void getinfo() { // display company information
            System.out.println("Name     :" + this.Name);
            System.out.println("Role     :" + this.Role);
            System.out.println("Salary   :" + this.salary + " lpa");
            System.out.println("CG cutoff:" + this.cg);
            System.out.println("Registered on ->" + glock.format(this.when));
        }

        public void apply_cmp() {
            System.out.print("Enter company Name   :");
            this.Name = sc.nextLine();
            System.out.print("Enter Role offering  :");
            this.Role = sc.nextLine();
            System.out.print("Enter Salary offering:");
            this.salary = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter CG criteria    :");
            this.cg = sc.nextDouble();
            sc.nextLine();
            System.out.println("applied");
            PlacementCell.addcmp(this);
        }

        public static void register_cmp(String name) { // portal registration for the company
            for (int i = 0; i < PlacementCell.cmpsize(); i++) {
                if (PlacementCell.cmpget(i).getName().equals(name)) {
                    System.out.println("Company already exsists");
                    return;
                }
            }
            for (int i = 0; i < PlacementCell.sizecmp(); i++) {
                if (PlacementCell.getcmp(i).getName().equals(name)) {
                    Company temp = PlacementCell.getcmp(i);
                    PlacementCell.cmpadd(temp);
                    temp.when = new Date();
                    System.out.println("Registered");
                    System.out.println(glock.format(temp.when));
                    return;
                }
            }
        }

        public void enroll() { // randomly select random number of students and display offer if best offer
                               // they have and status set to 1
            Random rand = new Random();
            int num_std = rand.nextInt(this.registered_students.size()) + 1;
            for (int i = 0; i < num_std; i++) {
                int std = rand.nextInt(this.registered_students.size());
                Student temp = this.registered_students.get(std);
                temp.getinfo();
                if (this.salary > temp.getoffr() && temp.getStatus() != -1 && this.salary >= 3 * temp.getctc()) {
                    temp.setStatus(1);
                    temp.setoffr(this);
                    this.selected_students.add(temp);
                }
                registered_students.remove(std);
            }

        }

        public void remstd(Student student) {
            this.registered_students.remove(student);
        }
    }

    public class PlacementCell {
        private static Date stdopen;
        private static Date stdclose;
        private static Date cmpopen;
        private static Date cmpclose;
        private static ArrayList<Student> students = new ArrayList<Student>();
        private static ArrayList<Company> companies = new ArrayList<Company>();
        private static ArrayList<Student> appliedstd = new ArrayList<Student>();
        private static ArrayList<Company> appliedcmp = new ArrayList<Company>();

        public static void setStdTime() throws ParseException {
            while (true) {
                System.out.println("Enter Student Registration open time in dd MMMM yyyy, HH:mm format");
                stdopen = glock.parse(sc.nextLine());
                // break;
                if (stdopen.compareTo(cmpclose) > 0)
                    break;
                else
                    System.out.println("Enter valid date ");

            }
            System.out.println("Enter Student Registration close time in dd MMMM yyyy, HH:mm format");
            stdclose = glock.parse(sc.nextLine());
        }

        public static void setCmpTime() throws ParseException {
            System.out.println("Enter Company Registration open time in dd MMMM yyyy, HH:mm format");
            cmpopen = glock.parse(sc.nextLine());
            System.out.println("Enter Company Registration close time in dd MMMM yyyy, HH:mm format");
            cmpclose = glock.parse(sc.nextLine());
        }

        public static Company cmpget(int i) {
            return companies.get(i);
        }

        public static int cmpsize() {
            return companies.size();
        }

        public static void cmpadd(Company cmpy) {
            companies.add(cmpy);
        }

        public static Student stdget(int i) {
            return students.get(i);
        }

        public static int stdsize() {
            return students.size();
        }

        public static void stdadd(Student stdu) {
            students.add(stdu);
        }

        public static Company getcmp(int i) {
            return appliedcmp.get(i);
        }

        public static int sizecmp() {
            return appliedcmp.size();
        }

        public static void addcmp(Company cmpy) {
            appliedcmp.add(cmpy);
        }

        public static Student getstd(int i) {
            return appliedstd.get(i);
        }

        public static int sizestd() {
            return appliedstd.size();
        }

        public static void addstd(Student stdu) {
            appliedstd.add(stdu);
        }

        public static int stdstatus(int num) {
            int out = 0;
            if (num == -1) {
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getStatus() == -1)
                        out++;
                }
            } else if (num == 0) {
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getCmp() == null)
                        out++;
                }
            } else if (num == 1) {
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getCmp() != null)
                        out++;
                }
            }
            return out;
        }

        public static void getStdDetl(String name, int roll) {
            for (int i = 0; i < students.size(); i++) {
                Student temp = students.get(i);
                if (temp.getName().equals(name) && temp.getRoll() == roll) {
                    temp.getinfo();
                    return;
                }
            }
        }

        public static Student getStd(String name, int roll) {
            for (int i = 0; i < appliedstd.size(); i++) {
                Student temp = appliedstd.get(i);
                if (temp.getName().equals(name) && temp.getRoll() == roll) {
                    return temp;
                }
            }
            return null;
        }

        public static Company getCmp(String name) {
            for (int i = 0; i < appliedcmp.size(); i++) {
                Company temp = appliedcmp.get(i);
                if ((temp.getName()).equals(name)) {
                    return temp;
                }
            }
            return null;
        }

        public static void getCmpDetl(String name) {
            for (int i = 0; i < companies.size(); i++) {
                Company temp = companies.get(i);
                if ((temp.getName()).equals(name)) {
                    temp.getinfo();
                    return;
                }
            }
        }

        public static void avgPack() {
            double net = 0;
            for (int i = 0; i < students.size(); i++) {
                Student temp = students.get(i);
                if (temp != null)
                    net += temp.getCmp().getSal();
            }
            double ans = net / students.size();
            System.out.println("Average Package :" + ans + "Lakhs");
        }

        public static void cmpyResult() {
            System.out.print("Enter Company name :");
            String plox = sc.nextLine();
            for (int i = 0; i < companies.size(); i++) {
                Company temp = companies.get(i);
                if (temp.getName().equals(plox)) {
                    System.out.println("following students enrolled in " + plox);
                    temp.enroll();
                }
            }
        }

    }

    public static void menuPrint(int num) throws ParseException {
        if (num == 0) {
            while (true) {
                System.out.println("Welcome to FutureBuilder:");
                System.out.println("    1) Enter the Application ");
                System.out.println("    2) Exit the Application ");
                System.out.print("index input :");
                int nep = sc.nextInt();
                sc.nextLine();
                if (nep == 1) {
                    menuPrint(1);
                } else {
                    break;
                }
            }
        } else if (num == 1) {
            while (true) {
                System.out.println("Choose The mode you want to Enter in:-");
                System.out.println("    1) Enter as Student Mode ");
                System.out.println("    2) Enter as Company Mode");
                System.out.println("    3) Enter as Placement Cell Mode");
                System.out.println("    4) Return To Main Application ");
                System.out.print("index input :");
                int nepm = sc.nextInt();
                sc.nextLine();
                if (nepm == 1) {
                    menuPrint(4);
                } else if (nepm == 2) {
                    menuPrint(3);
                } else if (nepm == 3) {
                    menuPrint(2);
                } else {
                    return;
                }
            }
        } else if (num == 2) {
            while (true) {
                System.out.println("Welcome to IIITD Placement Cell ");
                System.out.println("    1) Open Student Registrations");
                System.out.println("    2) Open Company Registrations ");
                System.out.println("    3) Get Number of Student Registrations");
                System.out.println("    4) Get Number of Company Registrations");
                System.out.println("    5) Get Number of Offered/Unoffered/Blocked Students");
                System.out.println("    6) Get Student Details ");
                System.out.println("    7) Get Company Details ");
                System.out.println("    8) Get Average Package");
                System.out.println("    9) Get Company Process Results ");
                System.out.println("    10)Back");
                System.out.print("index input :");
                int pcnep = sc.nextInt();
                sc.nextLine();
                if (pcnep == 3) {
                    System.out.println(PlacementCell.stdsize() + " Students Registered");
                } else if (pcnep == 4) {
                    System.out.println(PlacementCell.cmpsize() + " Companies Registered");
                } else if (pcnep == 5) {
                    System.out.println(PlacementCell.stdstatus(-1) + " Students Blocked");
                    System.out.println(PlacementCell.stdstatus(0) + " Students Unoffered");
                    System.out.println(PlacementCell.stdstatus(1) + " Students Offered");
                } else if (pcnep == 6) {
                    System.out.print("Enter Student Name    :");
                    String name = sc.nextLine();
                    System.out.print("Enter Student Roll no.:");
                    int roll = sc.nextInt();
                    sc.nextLine();
                    PlacementCell.getStdDetl(name, roll);
                } else if (pcnep == 7) {
                    System.out.print("Enter Company Name:");
                    String name = sc.nextLine();
                    PlacementCell.getCmpDetl(name);
                } else if (pcnep == 8) {
                    PlacementCell.avgPack();
                } else if (pcnep == 9) {
                    PlacementCell.cmpyResult();
                } else if (pcnep == 1) {
                    PlacementCell.setStdTime();
                } else if (pcnep == 2) {
                    PlacementCell.setCmpTime();

                } else
                    return;
            }
        } else if (num == 3) {
            while (true) {
                System.out.println("Choose the Company Query to perform ");
                System.out.println("    1) Add Company and Details");
                System.out.println("    2) Choose Company");
                System.out.println("    3) Get Available Companies");
                System.out.println("    4) Back");
                System.out.print("index input :");
                int cpnep = sc.nextInt();
                sc.nextLine();
                if (cpnep == 1) {
                    Company temp = new Company();
                    temp.apply_cmp();
                } else if (cpnep == 2) {
                    menuPrint(5);
                } else if (cpnep == 3) {
                    for (int i = 0; i < PlacementCell.cmpsize(); i++) {
                        Company temp = PlacementCell.cmpget(i);
                        temp.getinfo();
                        System.out.println();
                    }
                } else
                    return;

            }
        } else if (num == 5) {
            System.out.print("Enter Company Name :");
            String namu = sc.nextLine();
            while (true) {
                System.out.println("Welcome " + namu);
                System.out.println("    1) Update Role");
                System.out.println("    2) Update Package ");
                System.out.println("    3) Update CGPA criteria");
                System.out.println("    4) Register To Institute Drive ");
                System.out.println("    5) Back");
                System.out.print("index input :");
                int cinep = sc.nextInt();
                sc.nextLine();
                if (cinep == 1) {
                    PlacementCell.getCmp(namu).setRole();
                } else if (cinep == 2) {
                    PlacementCell.getCmp(namu).setPackage();
                } else if (cinep == 3) {
                    PlacementCell.getCmp(namu).setCGPA();
                } else if (cinep == 4) {
                    Date tik = new Date();
                    if (tik.compareTo(PlacementCell.cmpclose) < 0 &&
                            tik.compareTo(PlacementCell.cmpopen) > 0)
                        Company.register_cmp(namu);
                    else
                        System.out.println("Registration not possible at this time");
                } else
                    return;
            }
        } else if (num == 4) {
            while (true) {
                System.out.println("Choose the Student Query to perform");
                System.out.println("    1) Enter as a Student");
                System.out.println("    2) Add students");
                System.out.println("    3) Back");
                System.out.print("index input :");
                int snnep = sc.nextInt();
                sc.nextLine();
                if (snnep == 1) {
                    menuPrint(6);
                } else if (snnep == 2) {
                    System.out.print("Number of Students :");
                    int bruh = sc.nextInt();
                    sc.nextLine();
                    for (int j = 0; j < bruh; j++) {
                        System.out.println();
                        Student std = new Student();
                        std.apply_std();
                    }
                } else
                    return;
            }
        } else if (num == 6) {
            System.out.print("Enter Student Name :");
            String namu = sc.nextLine();
            System.out.print("Enter Roll no.     :");
            int rolu = sc.nextInt();
            sc.nextLine();
            while (true) {
                System.out.println("Welcome " + namu);
                System.out.println("    1) Register For Placement Drive ");
                System.out.println("    2) Register For Company");
                System.out.println("    3) Get All available companies ");
                System.out.println("    4) Get Current Status");
                System.out.println("    5) Update CGPA ");
                System.out.println("    6) Accept/Reject offer ");
                System.out.println("    7) Back");
                System.out.print("index input :");
                int sinep = sc.nextInt();
                sc.nextLine();
                if (sinep == 1) {
                    Date tok = new Date();
                    if (tok.compareTo(PlacementCell.stdclose) < 0 &&
                            tok.compareTo(PlacementCell.stdopen) > 0)
                        Student.register_std(namu, rolu);
                    else
                        System.out.println("Registration not possible at this time");
                } else if (sinep == 2) {
                    System.out.print("Enter Company Name :");
                    String cname = sc.nextLine();
                    PlacementCell.getStd(namu, rolu).cmp_register(PlacementCell.getCmp(cname));

                } else if (sinep == 3) {
                    for (int i = 0; i < PlacementCell.cmpsize(); i++) {
                        Company temp = PlacementCell.cmpget(i);
                        temp.getinfo();
                        System.out.println();
                    }
                } else if (sinep == 4) {
                    PlacementCell.getStd(namu, rolu).showStatus();
                } else if (sinep == 5) {

                    PlacementCell.getStd(namu, rolu).setcg();
                } else if (sinep == 6) {
                    PlacementCell.getStd(namu, rolu).accept();
                } else
                    return;
            }

        }

    }

    public static void main(String[] args) throws ParseException {
        menuPrint(0);
    }

}