#!/bin/bash

   # 결과 파일 이름 설정
   output_file="combined_java_files.txt"

   # 이전 결과 파일이 있다면 삭제
   rm -f "$output_file"

   # 현재 디렉토리와 모든 하위 디렉토리에서 .java 파일 찾기
   find . -name "*.java" | while read -r file; do
     # 파일 이름을 결과 파일에 추가
     echo "File: $file" >> "$output_file"
     echo "--------" >> "$output_file"
     
     # 파일 내용을 결과 파일에 추가
     cat "$file" >> "$output_file"
     
     # 구분선 추가
     echo -e "\n\n--------\n\n" >> "$output_file"
   done

   echo "모든 Java 파일이 $output_file 에 결합되었습니다."
   