
Pod::Spec.new do |s|
  s.name         = "RNGLModelViewer"
  s.version      = "1.0.0"
  s.summary      = "React Native native bridge to GLView - display and animate Wavefront .OBJ 3D models"
  s.description  = <<-DESC
                  react-native-gl-model-viewer
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author       = { "author" => "Michael Straßburger <codepoet@cpan.org>" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/rastaoasta/react-native-gl-model-viewer.git", :tag => "master" }
  s.source_files  = "RNGLModelViewer/**/*.{h,m}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "GLView"
end
