setl spell spelllang=en
syn match patchiMacro '\$([^)]*)' contains=@NoSpell
syn region patchiItem start='\$(\(item\|thing\|piece\))' end='\$(0)\|}' contains=patchiMacro keepend
syn region patchiEmph start='\$([k-n])' end='\$()' contains=patchiMacro keepend
syn region patchiItal start='\$(o)' end='\$()' contains=patchiMacro keepend
hi def link patchiMacro Special
hi def link patchiItem Identifier
hi patchiEmph cterm=bold gui=bold
hi patchiItal cterm=italic gui=italic
vnoremap <C-l> <Esc>`>a$(/l)<Esc>`<i$(l:)<left>
vnoremap <C-e> <Esc>m``>a$(0)<Esc>`<i$(item)<Esc>``7l
inoremap <C-e> $(item)$(0)<Left><C-o>3h
vnoremap <C-t> <Esc>m``>a$(0)<Esc>`<i$(thing)<Esc>``8l
inoremap <C-t> $(thing)$(0)<Left><C-o>3h
vnoremap <C-p> <Esc>m``>a$(0)<Esc>`<i$(piece)<Esc>``8l
inoremap <C-p> $(piece)$(0)<Left><C-o>3h
inoremap <C-d> <Left><C-o>A
let &makeprg="sh update-book.sh; (cd ../../../; ./gradlew processResources)"
