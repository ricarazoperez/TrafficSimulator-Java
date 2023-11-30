package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;
import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static Integer _timeLimit;
	private static boolean gui = true;
	
	private static void parseArgs(String[] args) {

		// define the valid command line options
		
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseModeOption(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);
			

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
					  .desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg()
				.desc("Ticks to the simulator´s main loop (default value is " + _timeLimitDefaultValue).build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Select mode gui/console").build());
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		
		if (!gui && _inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTicksOption(CommandLine line) throws ParseException{
		if(line.hasOption("t")) {
			_timeLimit = Integer.parseInt(line.getOptionValue("t"));
		}else _timeLimit = _timeLimitDefaultValue;
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException{
		String arg = line.getOptionValue("m");
		if(arg == null) gui = true;
		else if(arg.equals("console")) gui = false;
		else if(arg != null && !arg.equals("gui") && arg.equals("console")) 	throw new ParseException("Invalid paramether for mode");
		
	}

	private static void initFactories() {

		// TODO complete this method to initialize _eventsFactory
		//Inicializamos factoria de estrategias semaforo
		ArrayList<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		//Inicializamos factoria de estrategias dequeue
		ArrayList<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		//Ahora lista de builders
		List<Builder<Event>> eventBuilders = new ArrayList<>();
		eventBuilders.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
		eventBuilders.add(new NewCityRoadEventBuilder());
		eventBuilders.add(new NewInterCityRoadEventBuilder());
		eventBuilders.add(new NewVehicleEventBuilder());
		eventBuilders.add(new SetWeatherEventBuilder());
		eventBuilders.add(new SetContClassEventBuilder());

		//creamos la builderBasedFactory con la lista de builders
		_eventsFactory = new BuilderBasedFactory<>(eventBuilders);

	}

	private static void startBatchMode() throws IOException, InvalidArgumentException, ExecutionException {
		// TODO complete this method to start the simulation
		
		InputStream in = new FileInputStream(new File(_inFile));
		OutputStream out = _outFile == null ? System.out : new FileOutputStream(new File(_outFile));
		TrafficSimulator sim = new TrafficSimulator();
		Controller ctrl = new Controller(sim, _eventsFactory);
		ctrl.loadEvents(in);
		ctrl.run(_timeLimit, out);
		in.close();
		System.out.println("Done!");

	}
	
	private static void startGUIMode() throws InvalidArgumentException, IOException {
		
		TrafficSimulator sim = new TrafficSimulator();
		Controller ctrl = new Controller(sim, _eventsFactory);
		if(_inFile != null) {
			InputStream in = new FileInputStream(new File(_inFile));
			ctrl.loadEvents(in);
			in.close();
		}
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		});
	}

	private static void start(String[] args) throws IOException, InvalidArgumentException, ExecutionException {
		initFactories();
		parseArgs(args);
		if(gui) startGUIMode();
		else startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
