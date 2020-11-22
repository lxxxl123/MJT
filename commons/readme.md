# gradle公共包
1, 直接打包方式  
- build.gradle - add : `apply plugin:"maven"`

- 执行 install


- 引用项目
setting.gradle
dependencies {
    compile 'chen.commons:commons:1.0.0'
}
---
- 缺点: 
  - 不利于频繁更新
  - 协同开发时必须有私有仓库


 2, 
- setting.gradle 
    include 'commons'
    project(':commons').projectDir =file('../commons')
  
- build.gradle
dependencies {
    compile project(':commons')
}