resolvers += Resolver.url("scalasbt snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
