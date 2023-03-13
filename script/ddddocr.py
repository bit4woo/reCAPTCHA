import ddddocr

ocr = ddddocr.DdddOcr(beta=True)

with open("C:\Users\dell\Downloads\kaptcha.jpg", 'rb') as f:
    image = f.read()

res = ocr.classification(image)
print(res)