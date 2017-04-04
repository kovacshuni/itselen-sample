
lazy val `itselen-simple` = (project in file("itselen-simple"))
  .settings(BuildConfig.commonSettings)
  .settings(
    Compile / mainClass := Some("com.hunorkovacs.itselen.simple.ItsElenApp"),
    libraryDependencies ++= Dependencies.ItselenSimple
  )

lazy val `itselen` = (project in file("."))
  .aggregate(
    `itselen-simple`
  )
