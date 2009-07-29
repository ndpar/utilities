#!/usr/local/bin/ruby -w

class Dir
  def self.empty? dir_name
    entries(dir_name).size == 2
  end
end

Dir["**/**"].each do |file_name|
  puts file_name if File.directory? file_name and Dir.empty? file_name
end
