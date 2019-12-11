# compress-unpack

### compress-unpack介绍
compress-unpack是文件、文件夹压缩解压工具类，可以支持压缩为如下格式：
- rar
- zip
- tar.gz

### rar格式的压缩与解压
### zip格式的压缩与解压
### tar.gz格式的压缩与解压

### 解压缩工具类的使用
**压缩、解压zip: **  
- 压缩
```
CompressTool.compressToZip("input/springboot-log", "compress-output/");
```
- 解压
```
File file = new File("input/springboot-log.zip");
UnpackTool.unpackZip(file, "unpack-output/");
```
**压缩、解压rar：**  
- 解压
```
File file = new File("input/学习.rar");
UnpackTool.unpackRar(file, "unpack-output/");
```
**压缩、解压tar.gz：**