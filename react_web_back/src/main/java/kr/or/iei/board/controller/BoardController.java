package kr.or.iei.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.or.iei.FileUtil;
import kr.or.iei.board.model.dao.BoardDao;
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
		String savepath = root + "board/";
		if (thumbnail != null) {
			String filename = thumbnail.getOriginalFilename();
			String filepath = fileUtil.getFilepath(savepath, filename, thumbnail);// 파일명 중복안되게 처리,FileUtil에서 업로드까지 처리
			b.setBoardImg(filepath);
		}
		ArrayList<BoardFile> fileList = new ArrayList<BoardFile>();// 빈 list를 만들어놓고 처리
		if (boardFile != null) {
			for (MultipartFile file : boardFile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtil.getFilepath(savepath, filename, file);
				BoardFile bf = new BoardFile();
				bf.setFilename(filename);
				bf.setFilepath(filepath);
				fileList.add(bf);
			}
		}
		int result = boardService.insertBoard(b, fileList);
		return result;
	}

	@GetMapping(value = "/view/{boardNo}")
	public Board view(@PathVariable int boardNo) {
		return boardService.selectOneBoard(boardNo);
	}

	// 파일 다운로드용 리턴 타입
	@GetMapping(value = "/filedown/{boardFileNo}")
	public ResponseEntity<Resource> filedown(@PathVariable int boardFileNo)
			throws FileNotFoundException, UnsupportedEncodingException {
		BoardFile boardFile = boardService.getBoardFile(boardFileNo);
		System.out.println(boardFile);
		String savepath = root + "board/";
		File file = new File(savepath + boardFile.getFilepath());// 직접 저장한 파일명을 알아야함
		Resource resource = new InputStreamResource(new FileInputStream(file));
		String encodeFile = URLEncoder.encode(boardFile.getFilename(), "UTF-8");
		// 파일을 되돌려 주기위한 설정
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Disposition", "attachment; filename=\"" + encodeFile + "\"");
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return ResponseEntity.status(HttpStatus.OK).headers(header).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@PostMapping(value = "/contentImg")
	// multipartFile 변수명은 form.append에서 준 키값으로 가져와야함
	public String contentImg(@ModelAttribute MultipartFile image) {
		String savepath = root + "board/editor/";
		String filename = image.getOriginalFilename();
		String filepath = fileUtil.getFilepath(savepath, filename, image);
		return "/board/editor/" + filepath;
		// return filepath를 하면 여러군데서 쓰기 힘드므로
	}

	@GetMapping(value = "/delete/{boardNo}")
	public int delete(@PathVariable int boardNo) {
		// 해당게시글 첨부파일 삭제를 위해 파일목록을 결과로 받음
		List<BoardFile> fileList = boardService.delete(boardNo);
		if (fileList != null) {
			String savepath = root + "board/";
			for (BoardFile boardFile : fileList) {
				File file = new File(savepath + boardFile.getFilepath());
				file.delete();
			}
			return 1;
		} else {
			return 0;
		}
	}

	@PostMapping(value = "/modify")
	public int modify(@ModelAttribute Board b, @ModelAttribute MultipartFile thumbnail,
			@ModelAttribute MultipartFile[] boardFile) {
		// Board table 업데이트
		// 썸네일이 들어오면 -> 썸네일교체, 썸네일 없으면 기존 썸네일로 덮어쓰기
		// Board_file 테이블 업데이트 -> 삭제한게 있으면 삭제, 추가한거 있으면 insert
		// 삭제한 파일 있으면 파일 물리적 삭제
		if (b.getBoardImg().equals("null")) {
			b.setBoardImg(null);
		}
		String savepath = root + "board/";
		if (thumbnail != null) {
			String filepath = fileUtil.getFilepath(savepath, thumbnail.getOriginalFilename(), thumbnail);
			b.setBoardImg(filepath);
		}
		ArrayList<BoardFile> fileList = new ArrayList<BoardFile>();
		if (boardFile != null) {
			for (MultipartFile file : boardFile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtil.getFilepath(savepath, filename, file);
				BoardFile bf = new BoardFile();
				bf.setBoardNo(b.getBoardNo());
				bf.setFilename(filename);
				bf.setFilepath(filepath);
				fileList.add(bf);
			}
		}
		// DB에서 삭제한 파일이 있으면 실제로도 삭제하기 위해서 List로 받아와야함
		List<BoardFile> delFileList = boardService.modify(b, fileList);
		if (delFileList != null) {
			for (BoardFile bf : delFileList) {
				File delFile = new File(savepath + bf.getFilepath());
				delFile.delete();
			}
			return 1;
		} else {
			return 0;
		}
	}

	@GetMapping(value = "/adminList/{reqPage}")
	public Map adminList(@PathVariable int reqPage) {
		return boardService.adminList(reqPage);
	}
	
	@PostMapping(value="/changeStatus")
	public int changeStatus(@RequestBody Board b) {
		return boardService.changeStatus(b);
	}
}
