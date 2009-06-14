#!/usr/local/bin/ruby -w
#
# Usage: mvn_classes.rb [log_file]

declared_classes = []
Dir.glob("#{Dir.pwd}/**/*.class").each do |file|
  declared_classes << "#{$1}".gsub(/\//, '.') if file =~ /.*target\/classes\/(.*?)\.class$/
end
puts "=== Declared classes ===", declared_classes

if log_file = ARGV[0] and File.exists? log_file
  loaded_classes = []
  File.foreach log_file do |line|
    loaded_classes << "#{$1}" if line =~ /^\[Loaded\s(.*?)\s/
  end
  puts "=== Declared but not loaded classes ===", declared_classes - loaded_classes
end
