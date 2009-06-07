require 'fileutils'

class Refactorer
  include FileUtils

  def initialize source_pkg, dest_pkg, work_dir = Dir.pwd
    @source_pkg, @dest_pkg, @work_dir = source_pkg, dest_pkg, work_dir
  end

  def refactor
    source_subpath = pkg_to_subpath @source_pkg
    source_paths = restore_paths source_subpath
    return if source_paths.empty?

    dest_subpath = pkg_to_subpath @dest_pkg
    source_paths.each do |source_path|
      from_path = source_path[:full_path]
      to_path = "#{source_path[:base_path]}#{dest_subpath}"
      move_path! from_path, to_path
    end
  end

private

  def pkg_to_subpath pkg_name
    pkg_name.gsub(/\./, '/')
  end

  def restore_paths sub_path
    result = []
    Dir.glob("#{@work_dir}/**/*").each do |path|
      result << {:base_path => "#{$`}", :full_path => path} if path =~ /(#{sub_path})$/ and File.directory? path
    end
    result
  end

  def move_path! from_path, to_path
    puts "Moving #{from_path}/* to #{to_path}"
    mkdir_p to_path if !File.exists? to_path
    mv Dir.glob("#{from_path}/*"), to_path
    remove_entry from_path
  end

end

refactorer = Refactorer.new 'apache.r.exp', 'ndpar.ruby'
refactorer.refactor