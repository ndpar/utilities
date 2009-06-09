require 'fileutils'

class Refactorer
  include FileUtils

  FILE_EXTENSIONS = %{ .java .groovy .xml .properties }

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

    rename_packages!
  end

private

  def pkg_to_subpath pkg_name
    pkg_name.gsub(/\./, '/')
  end

  def restore_paths sub_path
    result = []
    Dir.glob("#{@work_dir}/**/*").each do |path|
      result << {base_path: "#{$`}", full_path: path} if path =~ /(#{sub_path})$/ and File.directory? path
    end
    result
  end

  def move_path! from_path, to_path
    puts "Moving #{from_path}/* to #{to_path}"
    mkdir_p to_path unless File.exists? to_path
    mv Dir.glob("#{from_path}/*"), to_path
    remove_entry from_path
  end

  def rename_packages!
    source_pkg_pattern = @source_pkg.gsub(/\./, '\.')
    Dir.glob("#{@work_dir}/**/*").each do |file|
      if File.file? file and FILE_EXTENSIONS.include? File.extname(file)
        content = File.read file
        content.gsub! Regexp.new(source_pkg_pattern), @dest_pkg
        File.open(file,"w") {|fw| fw << content}
      end
    end
  end
end

refactorer = Refactorer.new 'apache.r.exp', 'ndpar.ruby'
refactorer.refactor