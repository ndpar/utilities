#!/usr/local/bin/ruby -w

class Dir
  def self.empty? dir_name
    entries(dir_name).size == 2
  end
end

class String
  def is_dir?
    File.directory? self
  end

  def is_empty_dir?
    Dir.empty? self
  end

  def is_not_empty_dir?
    !is_empty_dir?
  end

  def parent_dir_name
    split('/')[-2]
  end
end

require 'fileutils'

TEMP_DIR = "git-clones"

Dir.mkdir TEMP_DIR

Dir.glob("#{Dir.pwd}/**/.git").each do |file|
  if file.is_dir?
    puts "Cloning Git repo: #{file}"
    project_name = file.parent_dir_name
    system "git clone --bare #{file} #{TEMP_DIR}/#{project_name}.git"
    system "tar cvzf #{TEMP_DIR}.tgz #{TEMP_DIR}" if TEMP_DIR.is_not_empty_dir?
  end
end

FileUtils.rm_rf TEMP_DIR