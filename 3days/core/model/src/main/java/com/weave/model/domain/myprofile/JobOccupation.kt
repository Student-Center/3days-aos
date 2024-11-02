package com.weave.model.domain.myprofile

enum class JobOccupation(val enValue: String, val koValue: String) {
    BUSINESS_ADMIN("BUSINESS_ADMIN", "경영·관리"),
    SALES_MARKETING("SALES_MARKETING", "영업·마케팅"),
    RESEARCH_DEVELOPMENT("RESEARCH_DEVELOPMENT", "연구·개발"),
    IT_INFORMATION("IT_INFORMATION", "IT·정보통신"),
    FINANCE_ACCOUNTING("FINANCE_ACCOUNTING", "금융·회계"),
    MANUFACTURING_PRODUCTION("MANUFACTURING_PRODUCTION", "생산·제조"),
    EDUCATION_ACADEMIA("EDUCATION_ACADEMIA", "교육·학술"),
    LAW_ADMINISTRATION("LAW_ADMINISTRATION", "법률·행정"),
    MILITARY_SECURITY("MILITARY_SECURITY", "경찰·소방·군인"),
    HEALTHCARE_MEDICAL("HEALTHCARE_MEDICAL", "의료·보건"),
    MEDIA_ENTERTAINMENT("MEDIA_ENTERTAINMENT", "미디어·언론"),
    ARTS_DESIGN("ARTS_DESIGN", "예술·문화"),
    SPORTS("SPORTS", "스포츠"),
    CONSTRUCTION_ENGINEERING("CONSTRUCTION_ENGINEERING", "건설·토목"),
    TRANSPORTATION_LOGISTICS("TRANSPORTATION_LOGISTICS", "운송·물류"),
    AGRICULTURE_FARMING("AGRICULTURE_FARMING", "농림어업"),
    SERVICE_INDUSTRY("SERVICE_INDUSTRY", "서비스"),
    OTHER("OTHER", "기타");

    companion object {
        fun findFromKoValue(koValue: String): JobOccupation? =
            JobOccupation.entries.find { it.koValue == koValue }
    }
}