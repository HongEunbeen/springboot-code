package eun.example.sparta.service;

import eun.example.sparta.model.Folder;
import eun.example.sparta.model.Product;
import eun.example.sparta.model.User;
import eun.example.sparta.repository.FolderRepository;
import eun.example.sparta.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class FolderService {
    // 멤버 변수 선언
    private final FolderRepository folderRepository;
    private final ProductRepository productRepository;

    // 생성자: ProductFolderService() 가 생성될 때 호출됨
    @Autowired
    public FolderService(FolderRepository folderRepository, ProductRepository productRepository) {
        // 멤버 변수 생성
        this.folderRepository = folderRepository;
        this.productRepository = productRepository;
    }

    // 회원 ID 로 등록된 모든 폴더 조회
    public List<Folder> getFolders(User user) {
        return folderRepository.findAllByUser(user);
    }

    public List<Folder> createFolders(List<String> folderNameList, User user) {
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNameList);
        List<Folder> folderList = new ArrayList<>();
        for (String folderName : folderNameList) {
            if(!isExistFolderName(folderName, existFolderList)){
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            }
        }

        //folderList가 null이여도 Spring 에서 알아서 걸러준다.
        folderList = folderRepository.saveAll(folderList);
        return folderList;
    }

    public boolean isExistFolderName(String folderName, List<Folder> existFolderList){
        for(Folder existFolder : existFolderList){
            if(existFolder.getName().equals(folderName)) return true;
        }
        return false;
    }

    // 회원 ID 가 소유한 폴더에 저장되어 있는 상품들 조회
    public Page<Product> getProductsOnFolder(User user, int page, int size, String sortBy, boolean isAsc, Long folderId) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAllByUserIdAndFolderList_Id(user.getId(), folderId, pageable);
    }
}