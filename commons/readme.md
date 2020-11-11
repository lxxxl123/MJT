# gradle公共包
1, 直接打包方式  
- build.gradle - add : `apply plugin:"maven"`
- 执行 install
---
- 缺点: 
  - 不利于频繁更新
  - 协同开发时必须有私有仓库


 2, 