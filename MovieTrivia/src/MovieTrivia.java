import java.util.ArrayList;

import file.MovieDB;
import movies.Actor;
import movies.Movie;

/**
 * Movie trivia class providing different methods for querying and updating a movie database.
 */
public class MovieTrivia {
	
	/**
	 * Create instance of movie database
	 */
	MovieDB movieDB = new MovieDB();
	
	
	public static void main(String[] args) {
		
		//create instance of movie trivia class
		MovieTrivia mt = new MovieTrivia();
		
		//setup movie trivia class
		mt.setUp("moviedata.txt", "movieratings.csv");
	}
	
	/**
	 * Sets up the Movie Trivia class
	 * @param movieData .txt file
	 * @param movieRatings .csv file
	 */
	public void setUp(String movieData, String movieRatings) {
		//load movie database files
		movieDB.setUp(movieData, movieRatings);
		
		//print all actors and movies
		this.printAllActors();
		this.printAllMovies();
	}

	/**
	 * Prints a list of all actors and the movies they acted in.
	 */
	public void printAllActors () {
		System.out.println(movieDB.getActorsInfo());
	}
	
	/**
	 * Prints a list of all movies and their ratings.
	 */
	public void printAllMovies () {
		System.out.println(movieDB.getMoviesInfo());
	}
	
	
	// TODO add additional methods as specified in the instructions PDF

	public void insertActor (String actor, String[] movies, ArrayList<Actor> actorsInfo){
		// set up a flag
		boolean actorInList = false;
		// handle the whitespace and uppercase in actor
		actor = actor.strip().toLowerCase();
		// handle the white and uppercase in movies array
		for (int i = 0; i < movies.length; ++i){
			movies[i] = movies[i].strip().toLowerCase();
		}

		// check if the actor is in the actorsInfo list
		for (Actor act: actorsInfo){
			// the actor exist in the list
			if (act.getName().equals(actor)){
				actorInList = true;
				// check if the movie is in the movies array
				for (String mov: movies) {
					if (!act.getMoviesCast().contains(mov)) {
						act.getMoviesCast().add(mov);
					}
				}
			}
		}

		// the actor does not exist, add to the actorsInfo list
		if (!actorInList) {
			Actor newActor = new Actor(actor);
			for (String movie : movies) {
				newActor.getMoviesCast().add(movie);
			}

			// update the actorsInfo
			actorsInfo.add(newActor);
		}
	}

	public void insertRating(String movie, int[] ratings, ArrayList<Movie> moviesInfo){
		// return if ratings are illegal
		if (ratings == null || ratings.length != 2) return;
		if (ratings[0] < 0 || ratings[1] <0 || ratings[0] > 100 || ratings[1] >100) return;

		// set up a flag
		boolean movieInList = false;

		// handle the movie
		movie = movie.strip().toLowerCase();
		for (Movie mov: moviesInfo){
			// movie is in the moviesInfo list
			if (mov.getName().equals(movie)){
				movieInList = true;
				// update the rating
				mov.setCriticRating(ratings[0]);
				mov.setAudienceRating(ratings[1]);
			}
		}

		// the movie is not in the moviesInfo list, add to it
		if (!movieInList) {
			Movie newMovie = new Movie(movie, ratings[0], ratings[1]);
			moviesInfo.add(newMovie);
		}
	}

	public ArrayList<String> selectWhereActorIs (String actor, ArrayList<Actor> actorsInfo){
		// set up a flag
		boolean actorExist = false;
		ArrayList<String> emptyList = new ArrayList<>();
		ArrayList<String> movieList = new ArrayList<>();
		// handle the whitespace and uppercase in actor
		actor = actor.strip().toLowerCase();
		for (Actor act: actorsInfo){
			// the actor is in the actorsInfo list
			if (act.getName().equals(actor)){
				actorExist = true;
				movieList = act.getMoviesCast();
			}
		}

		if(!actorExist) return emptyList;

		else return movieList;
	}


	public ArrayList<String> selectWhereMovieIs(String movie, ArrayList<Actor> actorsInfo) {
		// handle the whitespace and uppercase in movie
		movie = movie.strip().toLowerCase();
		ArrayList <String> actorList = new ArrayList<>();
		// name exhaustion
		for (Actor actor : actorsInfo) {
			// movie exhaustion
			for (int j = 0; j < actor.getMoviesCast().size(); ++j) {
				// if this actor is in the cast
				if (actor.getMoviesCast().get(j).equalsIgnoreCase(movie)) {
					// add the actor's name into the new list
					actorList.add(actor.getName().strip().toLowerCase());
				}
			}
		}
		return actorList;
	}


	public ArrayList<String> selectWhereRatingIs (char comparison, int targetRating, boolean isCritic, ArrayList<Movie>moviesInfo){
		ArrayList <String> movieList = new ArrayList<>();
		// check if the targetRating is valid
		if (targetRating < 0 || targetRating > 100) return movieList;
		// check if the comparison is valid
		switch (comparison){
			case '>':
			case '=':
			case '<':
				break;
			default:
				return movieList;
		}

		for (Movie movie : moviesInfo) {
			// for each i, set up a new variable to record the rating of either critic's or audience's
			int critic = 0;
			// for each i, set up a new variable to record the name of the movie
			String name = movie.getName().strip().toLowerCase();
			// set critic to critic's rating when isCritic is true
			if (isCritic) critic = movie.getCriticRating();
				// set critic to audience's rating when isCritic is false
			else critic = movie.getAudienceRating();

			// add the movie name to the return list
			if (comparison == '=' && targetRating == critic) movieList.add(name);
			else if (comparison == '>' && critic > targetRating) movieList.add(name);
			else if (comparison == '<' && critic < targetRating) movieList.add(name);
		}
		return movieList;
	}

// More Fun Methods
	public ArrayList<String> getCoActors (String actor, ArrayList<Actor> actorsInfo){
		actor = actor.strip().toLowerCase();
		ArrayList<String> coActors = new ArrayList<>();
		ArrayList<String> actorSelf = selectWhereActorIs(actor, actorsInfo);
		for (String s : actorSelf) {
			coActors.addAll(selectWhereMovieIs(s, actorsInfo));
			coActors.remove(actor);
		}
		return coActors;
	}

	public ArrayList<String> getCommonMovie (String actor1, String actor2, ArrayList<Actor> actorsInfo){
		actor1 = actor1.strip().toLowerCase();
		actor2 = actor2.strip().toLowerCase();
		ArrayList<String> actor1Movies = selectWhereActorIs(actor1, actorsInfo);
		ArrayList<String> actor2Movies = selectWhereActorIs(actor2, actorsInfo);
		ArrayList<String> commonMovies = new ArrayList<>();

		for (String actor1Movie : actor1Movies) {
			if (actor2Movies.contains(actor1Movie)) {
				commonMovies.add(actor1Movie);
			}
		}
		return commonMovies;
	}

	public ArrayList<String> goodMovies (ArrayList<Movie> moviesInfo){
		ArrayList<String> movieCritic = selectWhereRatingIs('>', 84, true, moviesInfo);
		ArrayList<String> movieAudience = selectWhereRatingIs('>', 84, false, moviesInfo);
		ArrayList<String> commonGood = new ArrayList<>();

		for (String s: movieCritic){
			if (movieAudience.contains(s)){
				commonGood.add(s);
			}
		}
		return commonGood;
	}

	public ArrayList<String> getCommonActors(String movie1, String movie2, ArrayList<Actor> actorsInfo) {
		movie1 = movie1.strip().toLowerCase();
		movie2 = movie2.strip().toLowerCase();

		ArrayList<String> movie1Actor = selectWhereMovieIs(movie1, actorsInfo);
		ArrayList<String> movie2Actor = selectWhereMovieIs(movie2, actorsInfo);
		ArrayList<String> commonActors = new ArrayList<>();

		for (String s : movie1Actor) {
			if (movie2Actor.contains(s)) {
				commonActors.add(s);
			}
		}
		return commonActors;
	}

	public static double[] getMean(ArrayList<Movie> moviesInfo) {
		double result1 = 0;
		double result2 = 0;

		for (Movie movie : moviesInfo) {
			result1 += movie.getCriticRating();
			result2 += movie.getAudienceRating();
		}

		return new double[] {result1 / moviesInfo.size(), result2 / moviesInfo.size()};
	}
}
