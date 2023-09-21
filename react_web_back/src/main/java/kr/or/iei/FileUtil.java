package kr.or.iei;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component//객체를 미리 만들어둠
public class FileUtil {//파일 이름 중복 체크
	public String getFilepath(String savepath, String filename,MultipartFile uploadFile) {
		//filename -> test.txt
		// test		.txt
		String onlyFilename = filename.substring(0,filename.lastIndexOf(".")); //test
		String extention = filename.substring(filename.lastIndexOf("."));//.txt
		//실제 업로드 할 파일명을 저장할 변수
		String filepath = null;
		//파일명 중복되면 뒤에 붙일 숫자
		int count = 0;
		while(true) {
			if(count == 0) {
				filepath = onlyFilename+extention;
			}else {
				filepath = onlyFilename+"_"+count+extention;
			}
			//C:/Temp/react_web/board/+filepath
			File checkFile = new File(savepath+filepath);
			if(!checkFile.exists()) {
				try {
					uploadFile.transferTo(checkFile);
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			count++;
		}
		return filepath;
	}

	
}
