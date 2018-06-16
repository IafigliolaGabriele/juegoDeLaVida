package chapter5;

/**
 * Don't be daunted by the number of classes in this chapter -- most of them are
 * just simple containers for information, and only have a handful of properties
 * with setters and getters.
 * 
 * The real stuff happens in the GeneticAlgorithm class and the Timetable class.
 * 
 * The Timetable class is what the genetic algorithm is expected to create a
 * valid version of -- meaning, after all is said and done, a chromosome is read
 * into a Timetable class, and the Timetable class creates a nicer, neater
 * representation of the chromosome by turning it into a proper list of Classes
 * with rooms and professors and whatnot.
 * 
 * The Timetable class also understands the problem's Hard Constraints (ie, a
 * professor can't be in two places simultaneously, or a room can't be used by
 * two classes simultaneously), and so is used by the GeneticAlgorithm's
 * calcFitness class as well.
 * 
 * Finally, we overload the Timetable class by entrusting it with the
 * "database information" generated here in initializeTimetable. Normally, that
 * information about what professors are employed and which classrooms the
 * university has would come from a database, but this isn't a book about
 * databases so we hardcode it.
 * 
 * @author bkanber
 *
 */
public class TimetableGA {

    public static void main(String[] args) {
    	// Get a Timetable object with all the available information.
        Timetable timetable = initializeTimetable();
        
        // Initialize GA
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.9, 2, 5);
        
        // Initialize population
        Population population = ga.initPopulation(timetable);
        
        // Evaluate population
        ga.evalPopulation(population, timetable);
        
        // Keep track of current generation
        int generation = 1;
        
        // Start evolution loop
        while (ga.isTerminationConditionMet(generation, 1000) == false
            && ga.isTerminationConditionMet(population) == false) {
            // Print fitness
            System.out.println("G" + generation + " Best fitness: " + population.getFittest(0).getFitness() + " Overall fitness: "+ population.getPopulationFitness());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population, timetable);

            // Evaluate population
            ga.evalPopulation(population, timetable);

            // Increment the current generation
            generation++;
        }

        // Print fitness
        timetable.createClasses(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Final solution overall fitness: " + population.getPopulationFitness());
        System.out.println("Clashes: " + timetable.calcClashes());

        // Print classes
        System.out.println();
        Class classes[] = timetable.getClasses();
        int classIndex = 1;
        for (Class bestClass : classes) {
            System.out.println("Class " + classIndex + ":");
            System.out.println("Module: " + 
                    timetable.getModule(bestClass.getModuleId()).getModuleName());
            System.out.println("Group: " + 
                    timetable.getGroup(bestClass.getGroupId()).getGroupId());
            System.out.println("Room: " + 
                    timetable.getRoom(bestClass.getRoomId()).getRoomNumber());
            System.out.println("Professor: " + 
                    timetable.getProfessor(bestClass.getProfessorId()).getProfessorName());
            System.out.println("Time: " + 
                    timetable.getTimeslot(bestClass.getTimeslotId()).getTimeslot());
            System.out.println("-----");
            classIndex++;
        }
    }

    /**
     * Creates a Timetable with all the necessary course information.
     * 
     * Normally you'd get this info from a database.
     * 
     * @return
     */
	private static Timetable initializeTimetable() {
		// Create timetable
		Timetable timetable = new Timetable();

		// Set up rooms
		timetable.addRoom(1, "A1-001", 15);
		timetable.addRoom(2, "A1-002", 30);
		timetable.addRoom(4, "A1-003", 20);
		timetable.addRoom(5, "A1-004", 25);
                timetable.addRoom(6, "A1-005", 15);
		timetable.addRoom(7, "A1-006", 30);
		timetable.addRoom(8, "A1-007", 20);
		timetable.addRoom(9, "A1-008", 25);
                

		// Set up timeslots
                timetable.addTimeslot(1, "Lun 7:00 - 8:30");
                timetable.addTimeslot(2, "Lun 8:45 - 10:15");
                timetable.addTimeslot(3, "Lun 10:30 - 12:00");
                timetable.addTimeslot(4, "Lun 12:15 - 1:45");
                timetable.addTimeslot(5, "Lun 2:00 - 3:30");
                timetable.addTimeslot(6, "Lun 3:45 - 5:15");
                timetable.addTimeslot(7, "Mar 7:00 - 8:30");
                timetable.addTimeslot(8, "Mar 8:45 - 10:15");
                timetable.addTimeslot(9, "Mar 10:30 - 12:00");
                timetable.addTimeslot(10, "Mar 12:15 - 1:45");
                timetable.addTimeslot(11, "Mar 2:00 - 3:30");
                timetable.addTimeslot(12, "Mar 3:45 - 5:15");
                timetable.addTimeslot(13, "Mie 7:00 - 8:30");
                timetable.addTimeslot(14, "Mie 8:45 - 10:15");
                timetable.addTimeslot(15, "Mie 10:30 - 12:00");
                timetable.addTimeslot(16, "Mie 12:15 - 1:45");
                timetable.addTimeslot(17, "Mie 2:00 - 3:30");
                timetable.addTimeslot(18, "Mie 3:45 - 5:15");
                timetable.addTimeslot(19, "Jue 7:00 - 8:30");
                timetable.addTimeslot(20, "Jue 8:45 - 10:15");
                timetable.addTimeslot(21, "Jue 10:30 - 12:00");
                timetable.addTimeslot(22, "Jue 12:15 - 1:45");
                timetable.addTimeslot(23, "Jue 2:00 - 3:30");
                timetable.addTimeslot(24, "Jue 3:45 - 5:15");
                timetable.addTimeslot(25, "Vie 7:00 - 8:30");
                timetable.addTimeslot(26, "Vie 8:45 - 10:15");
                timetable.addTimeslot(27, "Vie 10:30 - 12:00");
                timetable.addTimeslot(28, "Vie 12:15 - 1:45");
                timetable.addTimeslot(29, "Vie 2:00 - 3:30");
                timetable.addTimeslot(30, "Vie 3:45 - 5:15");

		// Set up professors
		timetable.addProfessor(1, "Dr P Smith");
		timetable.addProfessor(2, "Mrs E Mitchell");
		timetable.addProfessor(3, "Dr R Williams");
		timetable.addProfessor(4, "Mr A Thompson");

		// Set up modules and define the professors that teach them
		timetable.addModule(1, "cs1", "Computacion Emergente", new int[] { 1, 2 });
		timetable.addModule(2, "en1", "Ingenieria de Software", new int[] { 1, 3 });
		timetable.addModule(3, "ma1", "Sistemas de Informacion", new int[] { 1, 2 });
		timetable.addModule(4, "ph1", "Bases de Datos", new int[] { 3, 4 });
		timetable.addModule(5, "hi1", "Estadistica", new int[] { 4 });
		timetable.addModule(6, "dr1", "Matematicas IV", new int[] { 1, 4 });
                timetable.addModule(7, "cs1", "Matematicas V", new int[] { 1, 2 });
		timetable.addModule(8, "en1", "Ecuaciones Diferenciales", new int[] { 1, 3 });
		timetable.addModule(9, "ma1", "Modelacion de Sistemas de Redes", new int[] { 1, 2 });
		timetable.addModule(10, "ph1", "Simulacion", new int[] { 3, 4 });
		timetable.addModule(11, "hi1", "Ingenieria Economica", new int[] { 4 });
		timetable.addModule(12, "dr1", "Sistemas de Apoyo", new int[] { 1, 4 });
                timetable.addModule(13, "ph1", "Proyecto de Ingenieria", new int[] { 3, 4 });
		timetable.addModule(14, "hi1", "Ingenieria Ambiental", new int[] { 4 });
		timetable.addModule(15, "dr1", "Modelos Estocasticos", new int[] { 1, 4 });

		// Set up student groups and the modules they take.
		timetable.addGroup(1, 10, new int[] { 1, 3, 4, 5, 7 });
		timetable.addGroup(2, 30, new int[] { 2, 3, 5, 6, 8, 9 });
		timetable.addGroup(3, 18, new int[] { 3, 4, 5, 10, 13, 15 });
		timetable.addGroup(4, 25, new int[] { 1, 4, 11, 12, 14 });
		timetable.addGroup(5, 20, new int[] { 2, 3, 5, 12, 14 });
		timetable.addGroup(6, 22, new int[] { 1, 4, 5, 13, 15 });
		timetable.addGroup(7, 16, new int[] { 1, 3, 8, 9, 14 });
		timetable.addGroup(8, 18, new int[] { 2, 6, 7, 10, 15 });
		timetable.addGroup(9, 24, new int[] { 1, 6, 8, 9, 11 });
		timetable.addGroup(10, 25, new int[] { 3, 4, 7, 8, 10 });
		return timetable;
	}
}
