class File
  def self.mode file
    dec_mode = lstat(file).mode & 0777
    sprintf '%o', dec_mode
  end
end

class Hash
  def append key, item
    if self[key]
      self[key] << item unless self[key].include? item
    else
      self[key] = [item]
    end
  end
end

def all_modes
  type_modes = {}
  Dir.glob("#{Dir.pwd}/**/*").each do |file|
    if File.file? file
      mode = File.mode file
      ext = File.extname file
      type_modes.append ext, mode
    end
  end
  type_modes
end

def ext_modes ext
  type_modes = {}
  Dir.glob("#{Dir.pwd}/**/*#{ext}").each do |file|
    if File.file? file
      type_modes[file] = File.mode file
    end
  end
  type_modes
end

def ext_mode_files ext, mode
  type_modes = []
  Dir.glob("#{Dir.pwd}/**/*#{ext}").each do |file|
    if File.file? file and mode == File.mode(file)
      type_modes << file
    end
  end
  type_modes
end

def chmod_for_ext! ext, from_mode, to_mode
  files = ext_mode_files ext, from_mode
  files.each do |file|
    puts file
    File.chmod to_mode.to_i(8), file
  end
end

case ARGV.size
when 0
  all_modes.each do |ext,modes|
    puts "#{ext}: #{modes}"
  end
when 1
  ext_modes(ARGV[0]).each do |file,mode|
    puts "#{mode}: #{file}"
  end
when 2
  puts ext_mode_files(ARGV[0], ARGV[1])
when 3
  chmod_for_ext! ARGV[0], ARGV[1], ARGV[2]
end
