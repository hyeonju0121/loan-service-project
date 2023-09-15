# 대출 도메인 API 서비스
대출에 필요한 상담, 신청, 심사, 집행, 상환 기능을 제공하는 REST API 입니다.

금융의 본질이자 많은 부분을 차지하고 있는 대출 도메인을 개발자 관점에서 알아보기 위해 프로젝트를 진행하였습니다.

<br>

## 프로젝트 주요 기능
- **대출 신청을 하기 위한 약관 동의, 신청 정보 입력, 서류 업로드 기능 제공**
- **대출 신청 입회 서류를 업로드 할 수 있는 파일 시스템 구현**
  - 스프링에서 제공하는 MultipartFile 인터페이스 사용
  - 대출 신청 건에 해당하는 하위 디렉토리 파일이 존재하지 않으면, 하위 디렉토리 파일을 생성 후 파일을 업로드할 수 있도록 함
- **상담 신청 > 대출 신청 > 대출 심사 등록 > 한도 부여 > 심사 체결 > 대출 집행 > 대출 상환 순으로 처리 진행**
- **soft delete 방식을 통한 삭제 처리**
  - 모든 도메인이 복원 가능할 수 있도록 soft delete 방식을 사용하여 DELETE 쿼리보다 안전한 방법으로 사용자에게 제공하는 서비스의 질을 높이고자 함
  - `@Where(clause="is_delete=false")` 을 통한 필터링을 걸어주어, true 인 데이터는 조회할 수 없도록 함
  
<br>

## 사용 기술 스택
`Java` `spring boot 2.7.1` `jdk11` `JPA` `h2` `MySQL` `lomobok` `modelmapper` `docker`

<br>

## 유스케이스
![loan-service drawio](https://github.com/hyeonju0121/loan-service-project/assets/67223214/4d6587d3-d044-4fab-b296-ed7a93c73c65)

<br>

## ERD
![loan](https://github.com/hyeonju0121/loan-service-project/assets/67223214/34f97312-85e9-47f3-89f4-9ea71e358561)

<br>

## API 명세
### 1. 대출 상담
- **대출 상담 등록   `POST /counsels`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
       ▫    상담 등록을 위한 이름, 전화번호, 이메일, 상담 내용, 주소, 상세주소, 우편번호 등이 포함되어야 한다.
      
          (example)
          Request Body :
          {
          "name": "홍길동",
          "cellPhone": "010-1111-2222",
          "email": "gildong@gmail.com",
          "memo": "대출 상담을 원합니다.",
          "address": "서울특별시 강남구",
          "addressDetail": "OOO호",
          "zipCode": "11122"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
            "result": {
                "code": "0000",
                "desc": "success"
            },
            "data": {
                "counselId": 1,
                "name": "홍길동",
                "cellPhone": "010-1111-2222",
                "email": "gildong@gmail.com",
                "memo": "대출 상담을 원합니다.",
                "address": "서울특별시 강남구",
                "addressDetail": "OOO호",
                "zipCode": "11122",
                "appliedAt": "2023-09-16T00:54:44.7181802",
                "createdAt": "2023-09-16T00:54:44.7381799",
                "updatedAt": "2023-09-16T00:54:44.7381799"
            }
        }

  </div>
   
    </details>
- **대출 상담 조회   `GET /counsels/{counselId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /counsels/1

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "counselId": 1,
                  "name": "홍길동",
                  "cellPhone": "010-1111-2222",
                  "email": "gildong@gmail.com",
                  "memo": "대출 상담을 원합니다.",
                  "address": "서울특별시 강남구",
                  "addressDetail": "OOO호",
                  "zipCode": "11122",
                  "appliedAt": "2023-09-16T00:58:47.227713",
                  "createdAt": "2023-09-16T00:58:47.250711",
                  "updatedAt": "2023-09-16T00:58:47.250711"
              }
          }

  </div>
   
    </details>
- **대출 상담 수정   `PUT /counsels/{counselId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /counsels/1
  
          Request Body :
          {
          "name": "홍길동",
          "cellPhone": "010-1111-2222",
          "email": "gildong@gmail.com",
          "memo": "대출 상담을 원합니다. 연락 주세요.",
          "address": "서울특별시 강남구",
          "addressDetail": "OOO호",
          "zipCode": "11122"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
            "result": {
                "code": "0000",
                "desc": "success"
            },
            "data": {
                "counselId": 1,
                "name": "홍길동",
                "cellPhone": "010-1111-2222",
                "email": "gildong@gmail.com",
                "memo": "대출 상담을 원합니다. 연락 주세요.",
                "address": "서울특별시 강남구",
                "addressDetail": "OOO호",
                "zipCode": "11122",
                "appliedAt": "2023-09-16T00:58:47.227713",
                "createdAt": "2023-09-16T00:58:47.250711",
                "updatedAt": "2023-09-16T01:08:26.4099299"
            }
        }

  </div>
   
    </details>
- **대출 상담 삭제   `DELETE /counsels/{counselId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /counsels/1

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>

<br>

### 2. 대출 신청
- **대출 신청 등록   `POST /applications`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
       ▫    대출 신청을 위한 이름, 전화번호, 이메일, 희망 금액 등이 포함되어야 한다.
      
          (example)
          Request Body :
          {
              "name": "홍길동",
              "cellPhone": "010-1111-2222",
              "email": "gildong@gmail.com",
              "hopeAmount": "8000000"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "applicationId": 1,
                  "name": "홍길동",
                  "cellPhone": "010-1111-2222",
                  "email": "gildong@gmail.com",
                  "hopeAmount": 8000000,
                  "appliedAt": "2023-09-16T01:12:04.9683264",
                  "contractedAt": null,
                  "createdAt": "2023-09-16T01:12:04.9723261",
                  "updatedAt": "2023-09-16T01:12:04.9723261"
              }
          }

  </div>
   
    </details>
- **대출 신청 조회   `GET /applications/{applicationId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /applications/1

    </div>

    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "applicationId": 1,
                  "name": "홍길동",
                  "cellPhone": "010-1111-2222",
                  "email": "gildong@gmail.com",
                  "hopeAmount": 8000000,
                  "appliedAt": "2023-09-16T01:12:04.9683264",
                  "contractedAt": null,
                  "createdAt": "2023-09-16T01:12:04.9723261",
                  "updatedAt": "2023-09-16T01:12:04.9723261"
              }
          }

  </div>
   
    </details>
- **대출 신청 수정   `GET /applications/{applicationId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /applications/1

          Request Body :
          {
              "name": "홍길동",
              "cellPhone": "010-1111-2222",
              "email": "gildong@gmail.com",
              "hopeAmount": "5000000"
          }

    </div>

    <div markdown="1">    
      
          Response Body :
          {
            "result": {
                "code": "0000",
                "desc": "success"
            },
            "data": {
                "applicationId": 1,
                "name": "홍길동",
                "cellPhone": "010-1111-2222",
                "email": "gildong@gmail.com",
                "hopeAmount": 5000000,
                "appliedAt": "2023-09-16T01:12:04.968326",
                "contractedAt": null,
                "createdAt": "2023-09-16T01:12:04.972326",
                "updatedAt": "2023-09-16T01:15:58.8015918"
            }
        }

  </div>
   
    </details>
- **대출 신청 삭제   `DELETE /applications/{applicationId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /applications/1

    </div>

    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>
- **대출 신청 서류 업로드   `POST /applications/{applicationId}/files`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /applications/1

          file : 신청서류.hwp

    </div>

    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>
- **대출 신청 서류 다운로드   `GET /applications/{applicationId}/files?fileName={fileName}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /applications/1/files?fileName=신청서류.hwp

    </div>

    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>
- **대출 신청 서류 조회   `GET /applications/{applicationId}/files/info`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /applications/1/files/info

    </div>

    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": [
                  {
                      "name": "신청서류.hwp",
                      "url": "http://localhost:8080/applications/1/files?fileName=신청서류.hwp"
                  }
              ]
          }

  </div>
   
    </details>
- **대출 신청 서류 삭제   `DELETE /applications/{applicationId}/files`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   
 
          (example)
          url : /applications/1/files

    </div>

    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>

<br>

### 3. 이용 약관
- **이용 약관 등록   `POST /terms`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          Request Body :
          {
              "name": "대출 이용약관 1",
              "termsDetailUrl": "https://abc-storage.acc/djkfkgkfk"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "termsId": 1,
                  "name": "대출 이용약관 1",
                  "termsDetailUrl": "https://abc-storage.acc/djkfkgkfk",
                  "createdAt": "2023-09-16T02:39:00.9062106",
                  "updatedAt": "2023-09-16T02:39:00.9062106"
              }
          }

  </div>
   
    </details>
- **이용 약관 전체 조회   `GET /terms`**
      <details>
    <summary> request/response </summary>

    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": [
                  {
                      "termsId": 1,
                      "name": "대출 이용약관 1",
                      "termsDetailUrl": "https://abc-storage.acc/djkfkgkfk",
                      "createdAt": "2023-09-16T02:39:00.906211",
                      "updatedAt": "2023-09-16T02:39:00.906211"
                  }
              ]
          }

  </div>
   
    </details>
- **사용자 이용 약관 동의   `POST /applications/{applicationId}/terms`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /applications/1/terms
  
          Request Body :
          {
              "acceptTermsIds": [1, 2, 3]
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
            "result": {
                "code": "0000",
                "desc": "success"
            },
            "data": true
        }

  </div>
   
    </details>

<br>
    
### 4. 대출 심사
- **대출 심사 등록   `POST /judgments`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          Request Body :
          {
              "applicationId": 1,
              "name": "이순신",
              "approvalAmount": "3000000"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "judgmentId": 1,
                  "applicationId": 1,
                  "name": "이순신",
                  "approvalAmount": 3000000,
                  "createdAt": "2023-09-16T02:43:09.3605967",
                  "updatedAt": "2023-09-16T02:43:09.3605967"
              }
          }

  </div>
   
    </details>
- **대출 심사 아이디로 대출 심사 조회   `POST /judgments/{judgmentId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /judgments/1

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "judgmentId": 1,
                  "applicationId": 1,
                  "name": "이순신",
                  "approvalAmount": 3000000.00,
                  "createdAt": "2023-09-16T02:43:09.360597",
                  "updatedAt": "2023-09-16T02:43:09.360597"
              }
          }

  </div>
   
    </details>
- **대출 신청 아이디로 대출 심사 조회   `POST /judgments/applications/{applicationId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /judgments/applications/1

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "judgmentId": 1,
                  "applicationId": 1,
                  "name": "이순신",
                  "approvalAmount": 3000000.00,
                  "createdAt": "2023-09-16T02:43:09.360597",
                  "updatedAt": "2023-09-16T02:43:09.360597"
              }
          }

  </div>
   
    </details>
- **대출 심사 수정   `PUT /judgments/{judgmentId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /judgments/1

          Request Body : 
          {
              "applicationId": 1,
              "name": "홍길동",
              "approvalAmount": "4000000"
          }

          
    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "judgmentId": 1,
                  "applicationId": 1,
                  "name": "홍길동",
                  "approvalAmount": 4000000,
                  "createdAt": "2023-09-16T02:43:09.360597",
                  "updatedAt": "2023-09-16T02:47:42.3836305"
              }
          }

  </div>
   
    </details>
- **대출 심사 삭제   `DELETE /judgments/{judgmentId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /judgments/1
  
          
    </div>


    <div markdown="1">    
      
          Response Body :
          {
            "result": {
                "code": "0000",
                "desc": "success"
            },
            "data": null
        }

  </div>
   
    </details>
- **대출 심사 승인 금액 부여   `PATCH /judgments/{judgmentId}/grants`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /judgments/1/grants
  
          
    </div>


    <div markdown="1">    
      
          Response Body :
          {
            "result": {
                "code": "0000",
                "desc": "success"
            },
            "data": {
                "applicationId": 1,
                "approvalAmount": 4000000.00,
                "createdAt": "2023-09-16T02:49:26.59928",
                "updatedAt": "2023-09-16T02:50:22.1294578"
            }
        }

  </div>
   
    </details>

<br>
    
### 5. 대출 집행
- **대출 계약 체결   `PUT /applications/{applicationId}/contract`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /applications/1/contract

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>
- **대출 집행 등록   `POST /internal/applications/{applicationId}/entries`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/1/entries

          Request Body :
          {
              "entryAmount": "4000000"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "entryId": 1,
                  "applicationId": 1,
                  "entryAmount": 4000000,
                  "createdAt": "2023-09-16T02:53:20.3704631",
                  "updatedAt": "2023-09-16T02:53:20.3704631"
              }
          }

  </div>
   
    </details>
- **대출 집행 조회   `GET /internal/applications/{applicationId}/entries`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/1/entries
  
    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "entryId": 1,
                  "applicationId": 1,
                  "entryAmount": 4000000.00,
                  "createdAt": "2023-09-16T02:53:20.370463",
                  "updatedAt": "2023-09-16T02:53:20.370463"
              }
          }

  </div>
   
    </details>
- **대출 집행 수정   `GET /internal/applications/entries/{entryId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/entries/1

          Request Body :
          {
              "entryAmount": "8000000"
          }
  
    </div>


    <div markdown="1">    
      
          Response Body :
          {
            "result": {
                "code": "0000",
                "desc": "success"
            },
            "data": {
                "entryId": 1,
                "applicationId": 1,
                "beforeEntryAmount": 4000000.00,
                "afterEntryAmount": 8000000,
                "updatedAt": 2023-09-16T02:53:20.370463
            }
        }

  </div>
   
    </details>
- **대출 집행 삭제   `DELETE /internal/applications/entries/{entryId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/entries/1
  
    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>

<br>

### 6. 대출 상환
- **대출 상환 등록   `POST /internal/applications/{applicationId}/repayments`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/1/repayments

          Request Body :
          {
            "repaymentAmount" : "200000"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "applicationId": 1,
                  "repaymentAmount": 200000,
                  "balance": 3800000.00,
                  "createdAt": "2023-09-16T02:59:47.0227805",
                  "updatedAt": "2023-09-16T02:59:47.0227805"
              }
          }

  </div>
   
    </details>
- **대출 상환 조회   `GET /internal/applications/{applicationId}/repayments`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/1/repayments

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": [
                  {
                      "repaymentId": 1,
                      "repaymentAmount": 200000.00,
                      "createdAt": "2023-09-16T02:59:47.022781",
                      "updatedAt": "2023-09-16T02:59:47.022781"
                  },
                  {
                      "repaymentId": 2,
                      "repaymentAmount": 200000.00,
                      "createdAt": "2023-09-16T03:00:14.399241",
                      "updatedAt": "2023-09-16T03:00:14.399241"
                  },
                  {
                      "repaymentId": 3,
                      "repaymentAmount": 200000.00,
                      "createdAt": "2023-09-16T03:00:16.490235",
                      "updatedAt": "2023-09-16T03:00:16.490235"
                  }
              ]
          }

  </div>
   
    </details>
- **대출 상환 수정   `PUT /internal/applications/repayments/{repaymentId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/repayments/1

          Request Body :
          {
            "repaymentAmount" : "300000"
          }

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": {
                  "applicationId": 1,
                  "beforeRepaymentAmount": 200000.00,
                  "afterRepaymentAmount": 300000,
                  "balance": 3300000.00,
                  "createdAt": "2023-09-16T02:59:47.022781",
                  "updatedAt": "2023-09-16T03:01:47.1374274"
              }
          }

  </div>
   
    </details>
- **대출 상환 삭제   `DELETE /internal/applications/repayments/{repaymentId}`**
      <details>
    <summary> request/response </summary>
    
    <div markdown="1">   

          (example)
          url : /internal/applications/repayments/1

    </div>


    <div markdown="1">    
      
          Response Body :
          {
              "result": {
                  "code": "0000",
                  "desc": "success"
              },
              "data": null
          }

  </div>
   
    </details>
