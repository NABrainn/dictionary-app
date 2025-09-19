window.formPositioner = window.formPositioner || {
    debug: false,
    handleOverflow(form, sourceWordQuantity) {
        if(!form) {
            return
        }
        const height = window.innerHeight;
        const width = window.innerWidth
        //reset position
        form.style.top = 32
        form.style.left = ''
        form.style.right = ''
        form.style.bottom = ''
        //handle bottom overflow
        for(let i = 0; i < 2; i++) {
            if(form.getBoundingClientRect().bottom > height) {
                if(this.debug) {
                    console.log('overflow bottom', form.getBoundingClientRect())
                }
                form.style.top = ''
                form.style.bottom = 32
            }
            //handle right overflow
            if(form.getBoundingClientRect().right > width) {
                if(this.debug) {
                    console.log('overflow right', form.getBoundingClientRect())
                }
                form.style.right = 8
            }

            //handle left underflow
            if(form.getBoundingClientRect().left < 0) {
                if(this.debug) {
                    console.log('overflow left', form.getBoundingClientRect())
                }
                form.style.right = -132
            }
            if(this.debug) {
                console.log('finally', form.getBoundingClientRect())
            }
        }
    }
}