# Julhaxx

### Running the application locally
install postgres.
Set the following environment variables:
```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

`SPRING_DATASOURCE_URL` Should be something like `jdbc:postgresql://localhost:5432/postgres`

Then go to the root folder (the same one that README.md is in) and run
`gradlew bootRun`

It seems like the easiest way to develop is to use intellij because of its great Kotlin support.

###Completing challenges
Send a request to /completeChallenge with the day and challenge number. The code for this is located at `HtmlController.kt`

Check `ChallengeCodes.kt` for the super secret challenge codes.

### Frontend
We use mustache for html and static hosting (in the /static folder) for static resources.

Each page is built by importing more generic mustache files. This way we can re-use a lot of mustache/html files at the cost of a bit more headache when re-designing things that affect the whole page.

Use the folder structure. Put frontend html having to do with days in /days etc...

We simply import all css and most javascript in the header of every page. Because of that, make sure to use specific css class names such as `user-profile-small-space` instead of just `small-space`.


### Database communication
We use JPA to map objects to the database. There is some beautiful magic going on. Don't worry it's perfectly safe maybe.

To create a new entity, simply copy the patterns seen in `Entities.kt`. 

To create a db repository object, copy the pattern in `Repositories.kt`. There's some extra magical magic happening there. Make sure to name the methods in the interfaces and it should work just fine.
