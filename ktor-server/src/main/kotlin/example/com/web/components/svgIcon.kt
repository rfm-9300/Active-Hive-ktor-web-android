package example.com.web.components

import kotlinx.html.*

fun HtmlBlockTag.svgIcon(icon: SvgIcon, classes: String = "", size: Int = 24) {
    span {
        unsafe {
            when (icon) {
                SvgIcon.MENU -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 $size $size" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
            """
                SvgIcon.CLOSE -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 $size $size" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
            """
                SvgIcon.SEARCH -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 $size $size" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
            """
                SvgIcon.LIKE -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 $size $size" stroke="currentColor">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 6.00019C10.2006 3.90317 7.19377 3.2551 4.93923 5.17534C2.68468 7.09558 2.36727 10.3061 4.13778 12.5772C5.60984 14.4654 10.0648 18.4479 11.5249 19.7369C11.6882 19.8811 11.7699 19.9532 11.8652 19.9815C11.9483 20.0062 12.0393 20.0062 12.1225 19.9815C12.2178 19.9532 12.2994 19.8811 12.4628 19.7369C13.9229 18.4479 18.3778 14.4654 19.8499 12.5772C21.6204 10.3061 21.3417 7.07538 19.0484 5.17534C16.7551 3.2753 13.7994 3.90317 12 6.00019Z" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            """
                SvgIcon.TIME -> +"""
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="1.5"/>
    <path d="M12 8V12L14.5 14.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
                                    """
                SvgIcon.DEFAULT -> +"""
                <svg class="$classes" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 $size $size" stroke="currentColor">
                    <rect x="4" y="4" width="16" height="16" rx="2" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            """
                SvgIcon.DELETE -> +"""
                <svg width='24' height='24' viewBox='0 0 24 24' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink'><rect width='24' height='24' stroke='none' fill='#000000' opacity='0'/>
                    <g transform="matrix(0.5 0 0 0.5 12 12)" >
                    <g style="" >
                    <g transform="matrix(1 0 0 1 1 0)" >
                    <path style="stroke: none; stroke-width: 1; stroke-dasharray: none; stroke-linecap: butt; stroke-dashoffset: 0; stroke-linejoin: miter; stroke-miterlimit: 4; fill: rgb(223,240,254); fill-rule: nonzero; opacity: 1;" transform=" translate(-21, -20)" d="M 21 24.15 L 8.857 36.293 L 4.707 32.143 L 16.85 20 L 4.707 7.857 L 8.857 3.707 L 21 15.85 L 33.143 3.707 L 37.293 7.857 L 25.15 20 L 37.293 32.143 L 33.143 36.293 z" stroke-linecap="round" />
                    </g>
                    <g transform="matrix(1 0 0 1 1 0)" >
                    <path style="stroke: none; stroke-width: 1; stroke-dasharray: none; stroke-linecap: butt; stroke-dashoffset: 0; stroke-linejoin: miter; stroke-miterlimit: 4; fill: rgb(71,136,199); fill-rule: nonzero; opacity: 1;" transform=" translate(-21, -20)" d="M 33.143 4.414 L 36.586 7.856999999999999 L 25.15 19.293 L 24.443 20 L 25.150000000000002 20.707 L 36.586 32.143 L 33.143 35.586 L 21.707 24.15 L 21 23.443 L 20.293 24.150000000000002 L 8.857 35.586 L 5.414 32.143 L 16.85 20.707 L 17.557 20 L 16.849999999999998 19.293 L 5.414 7.857 L 8.857 4.414 L 20.293 15.85 L 21 16.557 L 21.707 15.849999999999998 L 33.143 4.414 M 33.143 3 L 21 15.143 L 8.857 3 L 4 7.857 L 16.143 20 L 4 32.143 L 8.857 37 L 21 24.857 L 33.143 37 L 38 32.143 L 25.857 20 L 38 7.857 L 33.143 3 L 33.143 3 z" stroke-linecap="round" />
                    </g>
                    </g>
                    </g>
                    </svg>
                        """
                SvgIcon.EDIT -> +"""
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M18.3785 8.44975L11.4637 15.3647C11.1845 15.6439 10.8289 15.8342 10.4417 15.9117L7.49994 16.5L8.08829 13.5582C8.16572 13.1711 8.35603 12.8155 8.63522 12.5363L15.5501 5.62132M18.3785 8.44975L19.7927 7.03553C20.1832 6.64501 20.1832 6.01184 19.7927 5.62132L18.3785 4.20711C17.988 3.81658 17.3548 3.81658 16.9643 4.20711L15.5501 5.62132M18.3785 8.44975L15.5501 5.62132" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M5 20H19" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                    """
            }
        }
    }
}

enum class SvgIcon {
    MENU, CLOSE, SEARCH, DEFAULT, LIKE, TIME, DELETE, EDIT
}