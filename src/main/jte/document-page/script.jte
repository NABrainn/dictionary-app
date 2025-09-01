@param int selectedWordId
@param int documentId
@raw
<script class="
top-8 top-6 top-10 right-4 bottom-12 bottom-10 
border-b-secondary border-l-secondary border-t-secondary border-r-secondary border-l-transparent border-r-transparent border
border-l-2 border-r-2 border-t-2 border-b-2
px-0.5 pl-0.5 pr-0.5 w-[2px] min-w-[2px]">
    //ABOVE styles used here solely for tailwind live reload server to pick up

    if (!customElements.get('form-position-adjuster')) {
        class FormPositionAdjuster extends HTMLElement {

            static observedAttributes = ['selected-word-id'];
            debug = false

            positions = {
                left: 'left-2',
                right: '',
                top: 'top-10',
                bottom: ''
            }

            constructor() {
                super();
            }

            log() {
                this.debug = true
            }
            updatePositions(id) {                
                const form = this.#getTranslationForm(id);         
                this.#handleOverflow(form);
            }
            updateFormPosition(form) {
                this.#handleOverflow(form);
            }
            #updateFormData() {
                const wordId = this.getAttribute('selected-word-id')
                htmx.findAll('.left-' + wordId).forEach((input) => {
                    input.value = this.positions.left
                })
                htmx.findAll('.right-' + wordId).forEach((input) => {
                    input.value = this.positions.right
                })
                htmx.findAll('.top-' + wordId).forEach((input) => {
                    input.value = this.positions.top
                })
                htmx.findAll('.bottom-' + wordId).forEach((input) => {
                    input.value = this.positions.bottom
                })
            }
            #getTranslationForm(id) {
                return htmx.find('#translation-form-container-' +  id);
            }
            #handleOverflow(form) {
                this.#adjustPosition({
                    form: form,
                    left: 2,
                    top: 10
                })
                this.#handleNorthEastOverflow(form)
                this.#handleNorthWestOverflow(form)
                this.#handleSouthEastOverflow(form)
                this.#handleSouthWestOverflow(form)
                this.#handleSouthOverflow(form)                
            }

            #handleNorthEastOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowRight(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 8,
                        right: 4
                    });
                    if(this.debug) {
                        console.log('overflow NE')
                    }
                }

            }
            #handleNorthWestOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowLeft(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow NW')
                    }
                }
            }
            #handleSouthEastOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        bottom: 10,
                        right: 4
                    });
                    if(this.debug) {
                        console.log('overflow SE')
                    }
                }
            }
            #handleSouthWestOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowLeft(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow SW')
                    }
                }
            }

            #handleSouthOverflow(form) {
                if(!form) {
                    return
                }
                if (!this.#isOverflowLeft(form) && !this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        bottom: 10
                    });
                    if(this.debug) {
                        console.log('overflow S')
                    }
                }
            }

            #isOverflowLeft(form) {
                if(!form) {
                    return
                }
                if(this.debug) {
                    console.log('overflow left')
                }
                return form.getBoundingClientRect().left > window.innerWidth;
            }

            #isOverflowRight(form) {
                if(!form) {
                    return
                }
                if(this.debug) {
                    console.log('overflow right')
                }
                return form.getBoundingClientRect().right > window.innerWidth;
            }

            #isOverflowBottom(form) {   
                if(!form) {
                    return
                }           
                if(this.debug) {
                    console.log('overflow bottom')
                }
                return form.getBoundingClientRect().bottom > window.innerHeight;
            }

            #adjustPosition(config) {
                this.#cleanupPositions(config)
                const rightPos = this.#assignRightPosition(config.right);
                const leftPos = this.#assignLeftPosition(config.left);
                const bottomPos = this.#assignBottomPosition(config.bottom);
                const topPos = this.#assignTopPosition(config.top);
                const positions = {
                    left: leftPos ?? '',
                    right: rightPos ?? '',
                    top: topPos ?? '',
                    bottom: bottomPos ?? ''
                }

                for(const [key, position] of Object.entries(positions)) {
                    if(position !== '') {
                        this.#updateClassList(config.form, position)
                    }
                }
                this.positions = positions
                if(this.debug) {
                    console.log('positions updated: ', this.positions)
                }
            }

            #updateClassList(form, value) {
                if(!form) {
                    return
                }
                form.classList.add(value)
                if(this.debug) {
                    console.log('classList updated: ', form.classList)
                }
            }

            #assignRightPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "right-" + value;
                    }
                    return "-right-" + Math.abs(value)
                }
            }

            #assignLeftPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "left-" + value;
                    }
                    return "-left-" + Math.abs(value)
                }
            }

            #assignBottomPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "bottom-" + value;
                    }
                    return "-bottom-" + Math.abs(value)
                }
            }

            #assignTopPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "top-" + value;
                    }
                    return "-top-" + Math.abs(value)
                }
            }

            #cleanupPositions(config) {
                if(!config) {
                    return
                }
                if(!config.form) {
                    return
                }
                const positionClasses = Array.from(config.form.classList).filter(cls =>
                    cls.match(/^(right-|left-|top-|bottom-|-right-|-left-|-top-|-bottom-)\d+$/)
                );
                positionClasses.forEach(cls => config.form.classList.remove(cls));
                if(this.debug) {
                    console.log('positions cleaned up: ', positionClasses)
                }
            }
        }

        customElements.define('form-position-adjuster', FormPositionAdjuster);
    }
@endraw
</script>
<form-position-adjuster selected-word-id="0"></form-position-adjuster>




