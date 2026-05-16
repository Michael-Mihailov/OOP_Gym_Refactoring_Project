import java.util.*;

public class GymConsole
{
    // constants
    private final MembershipType DEFAULT_MEMBERSHIP_TYPE = MembershipType.COMMUNITY;
    private final DifficultyType DEFAULT_DIFFICULTY_TYPE = DifficultyType.BEGINNER;
    
    // fields
    private Scanner scanner = new Scanner(System.in);
    
    private MemberService memberService;
    private FitnessClassService fitnessClassService;
    private TrainerService trainerService;
    
    public GymConsole(Scanner scanner, MemberService memberService, FitnessClassService fitnessClassService, TrainerService trainerService)
    {
        this.scanner = scanner;
        
        this.memberService = memberService;
        this.fitnessClassService = fitnessClassService;
        this.trainerService = trainerService;
    }
    
    public void start()
    {
        mainMenu();
    }
    
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        
        IdentifiableRegistry<Member> memberRegistry = new IdentifiableRegistry<Member>();
        IdGenerator memberIdGenerator = new IdGenerator();
        MemberService memberService = new MemberService(memberRegistry, memberIdGenerator);
        
        IdentifiableRegistry<FitnessClass> fitnessClassRegistry = new IdentifiableRegistry<FitnessClass>();
        IdGenerator fitnessClassIdGenerator = new IdGenerator();
        FitnessClassService fitnessClassService = new FitnessClassService(fitnessClassRegistry, fitnessClassIdGenerator);
        
        IdentifiableRegistry<Trainer> trainerRegistry = new IdentifiableRegistry<Trainer>();
        IdGenerator trainerIdGenerator = new IdGenerator();
        TrainerService trainerService = new TrainerService(trainerRegistry, trainerIdGenerator);
        
        GymConsole gymConsole = new GymConsole(scanner, memberService, fitnessClassService, trainerService);
        gymConsole.start();
    }
    
    
    // Member-related functions ----------------------------------------------

    public void addMember() {
        System.out.println("\n=== Add New Member ===");
        String name = readNonempty(scanner, "Name: ");
        
        // print the membership options on one line
        System.out.print("Membership types: ");
        MembershipType[] membershipTypeOptions = MembershipType.values();
        String[] membershipTypeDisplayNames = new String[ membershipTypeOptions.length ];
        for (int i = 0; i < membershipTypeDisplayNames.length; i++) membershipTypeDisplayNames[i] = membershipTypeOptions[i].getDisplayName(); 
        System.out.print(String.join(", ", membershipTypeDisplayNames));
        System.out.println();
        
        // select a membership option
        System.out.print("Membership type: ");
        MembershipType membershipTypeSelection = selectNameableEnum(scanner, membershipTypeOptions);
        if (membershipTypeSelection == null) 
        {
            MembershipType defaultSelection = DEFAULT_MEMBERSHIP_TYPE;
            
            System.out.println("Unknown membership type. Defaulting to '" + defaultSelection.getDisplayName() + "'.");
            membershipTypeSelection = defaultSelection;
        }
        
        // create the member
        Member member = memberService.createMember(name, membershipTypeSelection);

        System.out.println("Member added with id " + member.getId());
    }
    
    public void listMembers()
    {
        System.out.println(memberService.receiptDetailed());
    }
    
    public void deactivateMember()
    {
        System.out.println("\n=== Deactivate Member ===");
        System.out.println(memberService.receiptBasic());
        
        int memberId = readInt(scanner, "Enter member id to deactivate: ", null, null);
        DeactivateMemberResult result = memberService.deactivateMember(memberId);
        
        switch (result)
        {
            case DeactivateMemberResult.NOT_FOUND:
                System.out.println("Member not found.");
                return;
            case DeactivateMemberResult.ALREADY_INNACTIVE:
                System.out.println("Member is already inactive.");
                return;
            case DeactivateMemberResult.SUCCESS:
                Member member = memberService.findMemberById(memberId);
                System.out.println("Member " + member.getName() + " is now inactive.");
                return;
            default:
                throw new RuntimeException("You broke the code dumbass");
        }
    }
    
    public void addChargeToMember()
    {
        System.out.println("\n=== Add Charge to Member ===");
        System.out.println(memberService.receiptBasic());
        
        int memberId = readInt(scanner, "Enter member id to charge: ", null, null);
        if (memberService.findMemberById(memberId) == null)
        {
            System.out.println("Member not found.");
            return;
        }
        
        System.out.print("Charge amount: $");
        String line = scanner.nextLine().trim();
        double amount;
        try {
            amount = Double.parseDouble(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        
        PaymentResult result = memberService.chargeMember(memberId, amount);
        if (result.getStatus() != PaymentResult.Status.SUCCESS) // we pre-check so it should always be a success
                throw new RuntimeException("You broke the code dumbass");
        
        Member member = memberService.findMemberById(memberId);
        System.out.printf("Added $%.2f to %s's balance.%n", amount, member.getName());
    }
    
    public void applyPaymentFromMember()
    {
        System.out.println("\n=== Record Payment ===");
        System.out.println(memberService.receiptBasic());
        
        int memberId = readInt(scanner, "Enter member id to charge: ", null, null);
        if (memberService.findMemberById(memberId) == null)
        {
            System.out.println("Member not found.");
            return;
        }
        
        System.out.print("Payment amount: $");
        String line = scanner.nextLine().trim();
        double amount;
        try {
            amount = Double.parseDouble(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        
        PaymentResult result = memberService.applyPaymentFromMember(memberId, amount);
        if (result.getStatus() != PaymentResult.Status.SUCCESS) // we pre-check so it should always be a success
                throw new RuntimeException("You broke the code dumbass");
        
        Member member = memberService.findMemberById(memberId);
        System.out.printf("Recorded payment of $%.2f from %s.%n", amount, member.getName());
    }
    
    
    // Class-related functions -----------------------------------------------
    
    public void createClass()
    {
        System.out.println("\n=== Create Fitness Class ===");
        String name = readNonempty(scanner, "Class name: ");
        
        // print the difficulty options on one line
        System.out.print("Difficulty (");
        DifficultyType[] difficultyTypeOptions = DifficultyType.values();
        String[] difficultyTypeDisplayNames = new String[ difficultyTypeOptions.length ];
        for (int i = 0; i < difficultyTypeDisplayNames.length; i++) difficultyTypeDisplayNames[i] = difficultyTypeOptions[i].getDisplayName(); 
        System.out.print(String.join("/", difficultyTypeDisplayNames));
        System.out.println("): ");
        
        // select a difficulty option
        DifficultyType difficultyTypeSelection = selectNameableEnum(scanner, difficultyTypeOptions);
        if (difficultyTypeSelection == null) 
        {
            DifficultyType defaultSelection = DEFAULT_DIFFICULTY_TYPE;
            
            System.out.println("Unknown difficulty. Defaulting to '" + defaultSelection.getDisplayName() + "'.");
            difficultyTypeSelection = defaultSelection;
        }
        
        // input capacity
        int capacity = readInt(scanner, "Capacity: ", 1, null);
        
        // create the fitness class
        FitnessClass fitnessClass = fitnessClassService.createClass(name, difficultyTypeSelection, capacity);

        System.out.println("Fitness class created with id " + fitnessClass.getId());
    }
    
    public void listClasses()
    {
        System.out.println(fitnessClassService.receiptDetailed());
    }
    
    public void enrollMemberInClass()
    {
        System.out.println("\n=== Enroll Member in Class ===");
        System.out.println(memberService.receiptBasic());
        
        int memberId = readInt(scanner, "Enter member id: ", null, null);
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        if (memberService.getMemberActiveStatus(memberId) == false) {
            System.out.println("Cannot enroll an inactive member.");
            return;
        }

        System.out.println(fitnessClassService.receiptBasic());
        
        int fitnessClassId = readInt(scanner, "Enter class id: ", null, null);
        FitnessClass fitnessClass = fitnessClassService.findClassById(fitnessClassId);
        if (fitnessClass == null) {
            System.out.println("Class not found.");
            return;
        }

        EnrollMemberResult result = fitnessClassService.enrollMemberInClass(memberService, memberId, fitnessClassId);
        
        switch (result.getStatus())
        {
            case EnrollMemberResult.Status.CLASS_FULL:
                System.out.println("Class is full.");
                return;
            case EnrollMemberResult.Status.ALREADY_ENROLLED:
                System.out.println("Member is already enrolled in this class.");
                return;
            case EnrollMemberResult.Status.SUCCESS:
                System.out.println("Enrolled " + member.getName() + " in " + fitnessClass.getName() + ".");
                PaymentResult paymentResult = result.getPaymentResult();
                System.out.printf(
                    "Charged $%.2f for class (base $%.2f, type: %s).%n",
                    paymentResult.getPaymentAmount(), paymentResult.getBasePaymentAmount(), member.getMembershipType().getDisplayName()
                );
                return;
            default:
                throw new RuntimeException("You broke the code dumbass");
        }
    }
    
    public void listClassRoster()
    {
        System.out.println("\n=== Class Roster ===");
        System.out.println(fitnessClassService.receiptBasic());
        
        int fitnessClassId = readInt(scanner, "Enter class id: ", null, null);
        System.out.println(fitnessClassService.listClassRoster(fitnessClassId));
    }
    
    
    // Trainer-related functions ---------------------------------------------

    public void addTrainer()
    {
        System.out.println("\n=== Add Trainer ===");
        String name = readNonempty(scanner, "Trainer name: ");
        String specialty = readNonempty(scanner, "Specialty (e.g., yoga, strength, cardio): ");
        
        Trainer trainer = trainerService.createTrainer(name, specialty);
        int trainerId = trainer.getId();
        
        System.out.println("Enter trainer availability (e.g., Mon 9-11). Leave blank to stop.");
        while (true) {
            System.out.print("Availability: ");
            String slot = scanner.nextLine().trim();
            if (slot.isEmpty()) {
                break;
            }
            trainerService.appendToTrainerSchedule(trainerId, slot);
        }
        
        System.out.println("Trainer added with id " + trainerId);
    }
    
    public void listTrainersDetailed()
    {
        System.out.println(trainerService.receiptDetailed());
    }
    
    public void updateTrainerSchedule()
    {
        System.out.println("\n=== Update Trainer Schedule ===");
        System.out.println(trainerService.receiptBasic());
        
        int trainerId = readInt(scanner, "Enter trainer id: ", null, null);
        Trainer trainer = trainerService.findTrainerById(trainerId);
        if (trainer == null) {
            System.out.println("Trainer not found.");
            return;
        }
        
        System.out.println("Current schedule:");
        for (String s : trainer.getScheduleEntries()) {
            System.out.println("- " + s);
        }
        
        System.out.println("1. Replace schedule");
        System.out.println("2. Add to schedule");
        int choice = readInt(scanner, "Choice: ", 1, 2);
        
        if (choice == 1) 
        {
            trainerService.clearTrainerSchedule(trainerId);
            System.out.println("Enter new availability (blank to finish):");
        }
        else
        {
            System.out.println("Enter additional availability (blank to finish):");
        }
        while (true) {
            System.out.print("Availability: ");
            String slot = scanner.nextLine().trim();
            if (slot.isEmpty()) {
                break;
            }
            trainerService.appendToTrainerSchedule(trainerId, slot);
        }
        
        System.out.println("Updated schedule:");
        for (String s : trainer.getScheduleEntries()) {
            System.out.println("- " + s);
        }
    }
    
    
    // Reporting / summaries -------------------------------------------------

    public void showSummaryReport() {
        System.out.println("\n=== Summary Report ===");
 
        System.out.println("Total members: " + memberService.memberCount());
        System.out.println("Active members: " + memberService.activeMemberCount());
        System.out.printf("Total outstanding balance: $%.2f%n", memberService.totalOutstandingBalance());

        System.out.println("\nClasses:");
        for (FitnessClass fitnessClass : fitnessClassService.getFitnessClassEntries()) {
            System.out.printf("- %s (%s): %d/%d enrolled%n",
                    fitnessClass.getName(), fitnessClass.getDifficultyType().getDisplayName(), fitnessClass.getEnrollmentEntries().size(), fitnessClass.getCapacity());
        }

        System.out.println("\nTrainers:");
        for (Trainer trainer : trainerService.getTrainerEntries()) {
            System.out.printf("- %s (%s)%n", trainer.getName(), trainer.getSpecialty());
        }
        System.out.println();
    }
    
    
    // Menus -----------------------------------------------------------------
    
    public void memberMenu()
    {
        Menu trainerMenu = new Menu("Member Menu", 
            List.of(
                new MenuOption("Add member", () -> {this.addMember(); this.pause(scanner);}),
                new MenuOption("List members", () -> {this.listMembers(); this.pause(scanner);}),
                new MenuOption("Deactivate member", () -> {this.deactivateMember(); this.pause(scanner);}),
                new MenuOption("Add charge to member", () -> {this.addChargeToMember(); this.pause(scanner);}),
                new MenuOption("Record payment from member", () -> {this.applyPaymentFromMember(); this.pause(scanner);}),
                new MenuOption("Back to main menu", () -> {}, true)
            )
        );
        
        runMenu(trainerMenu);
    }
    public void classesMenu()
    {
        Menu trainerMenu = new Menu("Classes Menu", 
            List.of(
                new MenuOption("Create fitness class", () -> {this.createClass(); this.pause(scanner);}),
                new MenuOption("List fitness classes", () -> {this.listClasses(); this.pause(scanner);}),
                new MenuOption("Enroll member in class", () -> {this.enrollMemberInClass(); this.pause(scanner);}),
                new MenuOption("Show class roster", () -> {this.listClassRoster(); this.pause(scanner);}),
                new MenuOption("Back to main menu", () -> {}, true)
            )
        );
        
        runMenu(trainerMenu);
    }
    public void trainerMenu()
    {
        Menu trainerMenu = new Menu("Trainer Menu", 
            List.of(
                new MenuOption("Add trainer", () -> {this.addTrainer(); this.pause(scanner);}),
                new MenuOption("List trainers", () -> {this.listTrainersDetailed(); this.pause(scanner);}),
                new MenuOption("Update trainer schedule", () -> {this.updateTrainerSchedule(); this.pause(scanner);}),
                new MenuOption("Back to main menu", () -> {}, true)
            )
        );
        
        runMenu(trainerMenu);
    }
    public void mainMenu()
    {
        Menu mainMenu = new Menu("Campus Fitness Center Management", 
            List.of(
                new MenuOption("Manage members", this::memberMenu),
                new MenuOption("Manage classes", this::classesMenu),
                new MenuOption("Manage trainers", this::trainerMenu),
                new MenuOption("Show summary report", () -> {this.showSummaryReport(); pause(scanner);}),
                new MenuOption("Exit", () -> {System.out.println("Goodbye!"); System.exit(0);}, true)
            )
        );
        
        runMenu(mainMenu);
    }
    
    private void runMenu(Menu menu) // runs the menu in a loop
    {
        boolean exitMenu = false;
        while (exitMenu == false)
        {
            System.out.println(menu);
        
            int choice = readInt(scanner, "Choice: ", 1, menu.numChoices());
            menu.runOption(choice); 
            
            exitMenu = menu.exitsMenu(choice);
        }
    }

    
    // Utility functions -----------------------------------------------------

    public static <T extends Enum<T> & NameableEnum> T selectNameableEnum(Scanner scanner, T[] options)
    {
        String selection = scanner.nextLine().trim().toLowerCase();
        for (T option : options)
        {
            if (selection.equals(option.getDisplayName()))
            {
                return option;
            }
        }
        
        return null;
    }
    
    public static int readInt(Scanner scanner, String prompt, Integer minValue, Integer maxValue) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (minValue != null && value < minValue) {
                    System.out.println("Please enter a value >= " + minValue);
                    continue;
                }
                if (maxValue != null && value > maxValue) {
                    System.out.println("Please enter a value <= " + maxValue);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    public static String readNonempty(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    public static void pause(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}