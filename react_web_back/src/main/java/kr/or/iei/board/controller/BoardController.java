package kr.or.iei.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.or.iei.FileUtil;
import kr.or.iei.board.model.service.BoardService;
import kr.or.iei.board.model.vo.Board;
import kr.or.iei.board.model.vo.BoardFile;

@RestController
@RequestMapping(value = "/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private FileUtil fileUtil;
	@Value("${file.root}")
	private String root;

	@GetMapping(value = "/list/{reqPage}")
	public Map list(@PathVariable int reqPage) {
		Map map = boardService.boardList(reqPage);
		return map;
	}

	@PostMapping(value = "/insert")
	// 첨부파일이 있을 때는 modelAttribute
	// vo와 맞는 키값은 title,detail이므로 썸네일과 첨부파일을 따로따로 받아와야함
	public int insertBoard(@ModelAttribute Board b, @ModelAttribute MultipartFile thumbnail,
			@ModelAttribute MultipartFile[] boardFile, @RequestAttribute String memberId) {
		b.setMemberId(memberId);
		String savepath = root+"board/";
		if (thumbnail != null) {
			String filename = thumbnail.getOriginalFilename();
			String filepath = fileUtil.getFilepath(savepath, filename,thumbnail);//파일명 중복안되게 처리,FileUtil에서 업로드까지 처리
			b.setBoardImg(filepath);
		}
		ArrayList<BoardFile> fileList = new ArrayList<BoardFile>();//빈 list를 만들어놓고 처리
		if (boardFile != null) {
			for(MultipartFile file : boardFile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtil.getFilepath(savepath, filename, file);
				BoardFile bf = new BoardFile();
				bf.setFilename(filename);
				bf.setFilepath(filepath);
				fileList.add(bf);
			}
		}
		int result = boardService.insertBoard(b,fileList);
		return result;
	}
	
	@GetMapping(value="/view/{boardNo}")
	public Board view(@PathVariable int boardNo) {
		return boardService.selectOneBoard(boardNo);
	}
	
	//파일 다운로드용 리턴 타입
	@GetMapping(value="/filedown/{boardFileNo}")
	public ResponseEntity<Resource> filedown(@PathVariable int boardFileNo) throws FileNotFoundException, UnsupportedEncodingException{
		BoardFile boardFile = boardService.getBoardFile(boardFileNo);
		System.out.println(boardFile);
		String savepath = root+"board/";
		File file = new File(savepath+boardFile.getFilepath());//직접 저장한 파일명을 알아야함
		Resource resource = new InputStreamResource(new FileInputStream(file));
		String encodeFile = URLEncoder.encode(boardFile.getFilename(),"UTF-8");
		//파일을 되돌려 주기위한 설정
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Disposition","attachment; filename=\""+encodeFile+"\"");
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return ResponseEntity
				.status(HttpStatus.OK)
				.headers(header)
				.contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
}
