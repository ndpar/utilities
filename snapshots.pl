#!/usr/bin/perl -w
# Usage: find . -name pom.xml | xargs cat | snapshots.pl

#use re 'debugcolor'; # uncomment to see lot of debug info

undef $/;
$text = <>;

while ($text =~ m{
        <dependency>                          \s*
          <groupId>([^<]+)</groupId>          \s*
          <artifactId>([^<]+)</artifactId>    \s*
          <version>(.+?-SNAPSHOT)</version>
          (?s:.*?)
        </dependency>
      }gx)
{
    print "$1:$2:$3\n";
}
