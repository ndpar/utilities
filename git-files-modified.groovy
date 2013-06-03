#!/usr/bin/env groovy

/*
 * Inspired by:
 * http://michaelfeathers.typepad.com/michael_feathers_blog/2011/01/measuring-the-closure-of-code.html
 */
 
changes = [:]

gitlog = 'git log --name-status -M --reverse'.execute()
gitlog.in.eachLine { line ->
    if (line =~ /^[ADMR][^a-z]/) {
        switch (line[0]) {
        case 'A' : changes[line.split()[1]] = 1
                   break
        case 'M' : if (changes[line.split()[1]]) changes[line.split()[1]]++
                   else changes[line.split()[1]] = 2
                   break
        case 'D' : if (changes[line.split()[1]]) changes[line.split()[1]]--
                   else changes[line.split()[1]] = 0
                   break
        case 'R' : changes[line.split()[2]] = changes.remove(line.split()[1]) ?: 1
        }
    }
}
result = new File('git.csv')
changes.sort{ a, b -> b.value <=> a.value }.each { key, value ->
    result << "${value},${key}\n"
}
