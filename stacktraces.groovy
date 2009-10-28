#!/usr/bin/env groovy

if (!args) {
    println 'Usage: stacktraces.groovy <path/to/logfile>'
    System.exit 0
}

class LogFile extends File {

    String entryStartPattern

    LogFile(String fileName, String entryStartPattern) {
        super(fileName)
        this.entryStartPattern = entryStartPattern
    }

    def eachEntry(closure) {
        def newEntry = true, entry = ''
        eachLine {
            if (newEntry) {
                entry += it
                newEntry = false
            } else if (it =~ entryStartPattern) {
                closure.call entry
                entry = it
            } else {
                entry += "\n${it}"
            }
        }
        closure.call entry
    }

    def eachStacktrace(closure) {
        eachEntry() {
            if (it =~ /(?m)^(?:\t| {8})at/) closure.call it
        }
    }
}

new LogFile(args[0], /^\d{4}-\d{2}-\d{2}/).eachStacktrace() {
    println it
}
