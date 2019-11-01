
Pod::Spec.new do |s|
  s.name         = "RNGLModelView"
  s.version      = "1.5.0"
  s.summary      = "React Native native bridge to GLView - display and animate Wavefront .OBJ 3D models"
  s.description  = <<-DESC
                  react-native-gl-model-view
                   DESC
  s.homepage     = "https://github.com/rastapasta/react-native-gl-model-view"
  s.license      = "MIT"
  s.author       = { "author" => "Michael Straßburger <codepoet@cpan.org>" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/rastapasta/react-native-gl-model-view.git", :tag => "master" }
  s.source_files  = "ios/*.{h,m}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "GLView"
end
