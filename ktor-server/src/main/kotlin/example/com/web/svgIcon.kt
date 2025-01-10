package example.com.web

import kotlinx.html.*

fun HtmlBlockTag.svgIcon(icon: String, classes: String = "") {
    span {
        unsafe {
            when (icon) {
                "menu" -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
            """
                "close" -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
            """
                "search" -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
            """
                else -> throw IllegalArgumentException("Unknown icon: $icon")
            }
        }
    }
}