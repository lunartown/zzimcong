//package com.zzimcong.product.application.service;
//
//import com.zzimcong.product.application.dto.CategoryDto;
//import com.zzimcong.product.common.exception.NotFoundException;
//import com.zzimcong.product.domain.entity.Category;
//import com.zzimcong.product.domain.repository.CategoryRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class CategoryServiceTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private CategoryService categoryService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllCategories() {
//        Category category1 = new Category();
//        category1.setId(1L);
//        category1.setName("Category 1");
//
//        Category category2 = new Category();
//        category2.setId(2L);
//        category2.setName("Category 2");
//        category2.setParentCategory(category1);
//
//        when(categoryRepository.findAllWithParent()).thenReturn(Arrays.asList(category1, category2));
//
//        List<CategoryDto> result = categoryService.getAllCategories();
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Category 1", result.get(0).getName());
//        assertEquals(1, result.get(0).getChildCategories().size());
//        assertEquals("Category 2", result.get(0).getChildCategories().get(0).getName());
//
//        verify(categoryRepository, times(1)).findAllWithParent();
//    }
//
//    @Test
//    void testCreateCategory() {
//        Category parentCategory = new Category();
//        parentCategory.setId(1L);
//        parentCategory.setPath("/1/");
//
//        Category newCategory = new Category();
//        newCategory.setId(2L);
//        newCategory.setName("New Category");
//        newCategory.setParentCategory(parentCategory);
//        newCategory.setPath("/1/2/");
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
//        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
//
//        Category result = categoryService.createCategory("New Category", 1L);
//
//        assertNotNull(result);
//        assertEquals(2L, result.getId());
//        assertEquals("New Category", result.getName());
//        assertEquals(parentCategory, result.getParentCategory());
//        assertEquals("/1/2/", result.getPath());
//
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(categoryRepository, times(1)).save(any(Category.class));
//    }
//
//    @Test
//    void testCreateRootCategory() {
//        Category newCategory = new Category();
//        newCategory.setId(1L);
//        newCategory.setName("Root Category");
//        newCategory.setPath("/1/");
//
//        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
//
//        Category result = categoryService.createCategory("Root Category", null);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("Root Category", result.getName());
//        assertNull(result.getParentCategory());
//        assertEquals("/1/", result.getPath());
//
//        verify(categoryRepository, never()).findById(anyLong());
//        verify(categoryRepository, times(1)).save(any(Category.class));
//    }
//
//    @Test
//    void testUpdateCategory() {
//        Category existingCategory = new Category();
//        existingCategory.setId(1L);
//        existingCategory.setName("Old Name");
//
//        CategoryDto updateDto = new CategoryDto();
//        updateDto.setName("New Name");
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
//        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        CategoryDto result = categoryService.updateCategory(1L, updateDto);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("New Name", result.getName());
//
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(categoryRepository, times(1)).save(any(Category.class));
//    }
//
//    @Test
//    void testUpdateCategoryNotFound() {
//        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
//
//        CategoryDto updateDto = new CategoryDto();
//        updateDto.setName("New Name");
//
//        assertThrows(NotFoundException.class, () -> categoryService.updateCategory(1L, updateDto));
//
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(categoryRepository, never()).save(any(Category.class));
//    }
//
//    @Test
//    void testDeleteCategory() {
//        Category category = new Category();
//        category.setId(1L);
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//
//        categoryService.deleteCategory(1L);
//
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(categoryRepository, times(1)).delete(category);
//    }
//
//    @Test
//    void testDeleteCategoryNotFound() {
//        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
//
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(categoryRepository, never()).delete(any(Category.class));
//    }
//
//    @Test
//    void testFindAllSubCategoryIds() {
//        Category parentCategory = new Category();
//        parentCategory.setId(1L);
//        parentCategory.setPath("/1/");
//
//        Category childCategory1 = new Category();
//        childCategory1.setId(2L);
//        childCategory1.setPath("/1/2/");
//
//        Category childCategory2 = new Category();
//        childCategory2.setId(3L);
//        childCategory2.setPath("/1/3/");
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
//        when(categoryRepository.findByPathStartingWith("/1/")).thenReturn(Arrays.asList(childCategory1, childCategory2));
//
//        List<Long> result = categoryService.findAllSubCategoryIds(1L);
//
//        assertNotNull(result);
//        assertEquals(3, result.size());
//        assertTrue(result.containsAll(Arrays.asList(1L, 2L, 3L)));
//
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(categoryRepository, times(1)).findByPathStartingWith("/1/");
//    }
//
//    @Test
//    void testFindAllSubCategoryIdsNotFound() {
//        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> categoryService.findAllSubCategoryIds(1L));
//
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(categoryRepository, never()).findByPathStartingWith(anyString());
//    }
//
//    @Test
//    void testGetCategoryById() {
//        Category category = new Category();
//        category.setId(1L);
//        category.setName("Test Category");
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//
//        Category result = categoryService.getCategoryById(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("Test Category", result.getName());
//
//        verify(categoryRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetCategoryByIdNotFound() {
//        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1L));
//
//        verify(categoryRepository, times(1)).findById(1L);
//    }
//}